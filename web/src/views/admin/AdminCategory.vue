<template>
    <a-layout>
        <a-layout-content
            :style="{
                background: '#fff',
                padding: '24px',
                margin: 0,
                minHeight: '280px',
            }"
        >
            <p>
                <a-form layout="inline" :model="param">
                    <a-form-item>
                        <a-input v-model:value="param.name" placeholder="名称">
                        </a-input>
                    </a-form-item>
                    <a-form-item>
                        <a-button
                            type="primary"
                            @click="
                                handleQuery({
                                    page: 1,
                                    size: pagination.pageSize,
                                })
                            "
                        >
                            查询
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()">
                            新增
                        </a-button>
                    </a-form-item>
                </a-form>
            </p>
            <a-table
                :columns="columns"
                :row-key="(record) => record.id"
                :data-source="categorys"
                :pagination="pagination"
                :loading="loading"
                @change="handleTableChange"
            >
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar" />
                </template>
                <template v-slot:action="{ text, record }">
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            编辑
                        </a-button>
                        <a-popconfirm
                            title="确认删除？"
                            ok-text="是"
                            cancel-text="否"
                            @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger"> 删除 </a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>

    <a-modal
        title="分类表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
        <a-form
            :model="category"
            :label-col="{ span: 6 }"
            :wrapper-col="{ span: 18 }"
        >
        
            <a-form-item label="名称">
                <a-input v-model:value="ebook.name" />
            </a-form-item>
            <a-form-item label="父分类">
                <a-input v-model:value="ebook.parent"></a-input>
            </a-form-item>
            
            <a-form-item label="顺序">
                <a-input v-model:value="ebook.sort" type="textarea" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import {Tool} from "@/utils/tool";
import facebookFilled from "@ant-design/icons-vue/lib/icons/FacebookFilled";



export default defineComponent({
    name: "AdminCategory",
  methods: {facebookFilled},
    setup() {
        const param = ref();
        param.value = {};
        const categorys = ref();
        const pagination = ref({
            current: 1,
            pageSize: 4,
            total: 0,
        });
        const loading = ref(false);

        const columns = [
            {
                title: "名称",
                dataIndex: "name",
            },
            {
                title: "文档数",
                key: "parent",
                dataIndex: "parent",
            },
            {
                title: "顺序",
                key: "sort",
                dataIndex: "sort",
            },
            {
                title: "Action",
                key: "action",
                slots: { customRender: "action" },
            },
        ];

        /**
         * 数据查询
         **/
        const handleQuery = (params: any) => {
            loading.value = true;
            // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
            // categorys.value = [];
            axios
                .get("/category/list", {
                    params: {
                        page: params.page,
                        size: params.size,  
                        name: param.value.name,
                    },
                })
                .then((response) => {
                    loading.value = false;
                    const data = response.data;

                    if (data.success) {
                        categorys.value = data.content.list;

                        // 重置分页按钮
                        pagination.value.current = params.page;
                        // pagination.value.total = data.content.total;
                        pagination.value.total = data.content.total;
                    } else {
                        message.error(data.message);
                    }
                });
        };

        // -------- 表单 ---------
        /**
         * 数组，[100, 101]对应：前端开发 / Vue
         */
        //   const categoryIds = ref();
        const category = ref({});
        const modalVisible = ref(false);
        const modalLoading = ref(false);
        const handleModalOk = () => {
            modalLoading.value = true;
            // category.value.category1Id = categoryIds.value[0];
            // category.value.category2Id = categoryIds.value[1];
            axios.post("/category/save", category.value).then((response) => {
                const data = response.data; // data = commonResp
                modalLoading.value = false;
                if (data.success) {
                    modalVisible.value = false;

                    // 重新加载列表
                    handleQuery({
                        page: pagination.value.current,
                        size: pagination.value.pageSize,
                    });
                } else {
                    message.error(data.message);
                }
            });

            // setTimeout(() => {
            //   modalLoading.value = false;
            //   modalVisible.value = false;
            // }, 2000);
        };

        /**
         * 编辑
         */
        const edit = (record: any) => {
            modalVisible.value = true;
            // category.value = record;
            category.value = Tool.copy(record);
            // categoryIds.value = [category.value.category1Id, category.value.category2Id]
        };

        /**
         * 新增
         */
        const add = () => {
            modalVisible.value = true;
            category.value = {};

            // category.value = Tool.copy(record);
            // categoryIds.value = [category.value.category1Id, category.value.category2Id]
        };

        /**
         * 删除
         */

        const handleDelete = (id: number) => {
            axios.delete("/category/delete/" + id).then((response) => {
                const data = response.data;
                if (data.success) {
                    // 重新加载列表
                    handleQuery({
                        page: pagination.value.current,
                        size: pagination.value.pageSize,
                    });
                }
                //   else {
                //     message.error(data.message);
                //   }
            });
        };

        /**
         * 表格点击页码时触发
         */
        const handleTableChange = (pagination: any) => {
            console.log("看看自带的分页参数都有啥：" + pagination);
            handleQuery({
                page: pagination.current,
                size: pagination.pageSize,
            });
        };

        onMounted(() => {
            handleQuery({
                // 这里的属性名要和后端接口的参数名一致，否则后端接收不到
                //  /category/list?page=1&size=4
                page: 1,
                size: pagination.value.pageSize,
            });
        });

        return {
            param,
            // message,
            categorys,
            pagination,
            columns,
            loading,
            handleTableChange,

            edit,
            add,
            handleDelete,

            category,
            modalLoading,
            handleQuery,
            modalVisible,
            handleModalOk,
        };
    },
});
</script>
