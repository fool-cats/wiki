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
                        <a-button type="primary" @click="handleQuery()">
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
                :data-source="level1"
                :pagination="false"
                :loading="loading"
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
                <a-input v-model:value="category.name" />
            </a-form-item>
            <a-form-item label="顺序">
                <a-input v-model:value="category.sort" type="textarea" />
           
            
            </a-form-item>
            <!-- 只展示一级分类 -->
            <a-form-item label="父分类">
                <!-- <a-input v-model:value="category.parent"></a-input> -->
                <a-select
                    ref="select"
                    v-model:value="category.parent"
                    style="width: 120px"
                >
                    <a-select-option value="0">无</a-select-option>
                    <!--  -->
                    <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id === c.id">
                    {{ c.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/utils/tool";
import FacebookFilled from "@ant-design/icons-vue/lib/icons/FacebookFilled";

export default defineComponent({
    name: "AdminCategory",
    methods: { FacebookFilled },
    setup() {
        const param = ref();
        param.value = {};
        const categorys = ref();
        // const pagination = ref({
        //     current: 1,
        //     pageSize: 10,
        //     total: 0,
        // });
        const loading = ref(false);

        const columns = [
            {
                title: "名称",
                dataIndex: "name",
            },
            {
                title: "父分类",
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

        // 一级分类树，children是二级分类

        /**
         * [{
         *  id: "".
         *  name: "",
         *  children: [{
         *      id: "",
         *      name: "",
         *
         *         }]
         * }]
         *
         */
        const level1 = ref();

        /**
         * 数据查询
         **/
        const handleQuery = () => {
            loading.value = true;
            // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
            categorys.value = [];
            axios.get("/category/all").then((response) => {
                loading.value = false;
                const data = response.data;

                if (data.success) {
                    categorys.value = data.content;
                    console.log("原始数据", categorys.value);

                    // 一级分类,存放数组

                    level1.value = [];
                    // 初始为，因为所有数据都是一级分类，一级分类的父id是000
                    level1.value = Tool.array2Tree(categorys.value, 0);

                    console.log("树形结构", level1);
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
                
                modalLoading.value = false;
                const data = response.data; // data = commonResp
                if (data.success) {
                    modalVisible.value = false;

                    // 重新加载列表
                    handleQuery();
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
                    handleQuery();
                }
                //   else {
                //     message.error(data.message);
                //   }
            });
        };

        onMounted(() => {
            handleQuery();
        });

        return {
            param,
            // message,
            categorys,
            columns,
            loading,
            level1,

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
