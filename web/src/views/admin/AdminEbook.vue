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
            <a-button type="primary" @click="add()" size="large"> 新增 </a-button>
        </p>
            <a-table
                :columns="columns"
                :row-key="(record) => record.id"
                :data-source="ebooks"
                :pagination="pagination"
                :loading="loading"
                @change="handleTableChange"
            >
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar" />
                </template>
                <template v-slot:action="{ text, record }">
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)"> 编辑 </a-button>
                        <a-button type="danger"> 删除 </a-button>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>

    <a-modal
        title="电子书表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
        <a-form

            :model="ebook"
            :label-col="{ span: 6 }"
            :wrapper-col="{ span: 18 }"
        >
            <a-form-item label="封面">
                <a-input v-model:value="ebook.cover" />
            </a-form-item>
            <a-form-item label="名称">
                <a-input v-model:value="ebook.name" />
            </a-form-item>
            <a-form-item label="分类">
                <a-cascader
                    v-model:value="categoryIds"
                    :field-names="{
                        label: 'name',
                        value: 'id',
                        children: 'children',
                    }"
                    :options="level1"
                />
            </a-form-item>
            <a-form-item label="描述">
                <a-input v-model:value="ebook.description" type="textarea" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";

export default defineComponent({
    name: "AdminEbook",
    setup() {
        // const param = ref();
        // param.value = {};
        const ebooks = ref();
        const pagination = ref({
            current: 1,
            pageSize: 4,
            total: 0,
        });
        const loading = ref(false);

        const columns = [
            {
                title: "封面",
                dataIndex: "cover",
                slots: { customRender: "cover" },
            },
            {
                title: "名称",
                dataIndex: "name",
            },
            {
                title: "分类",
                slots: { customRender: "category" },
            },
            {
                title: "文档数",
                dataIndex: "docCount",
            },
            {
                title: "阅读数",
                dataIndex: "viewCount",
            },
            {
                title: "点赞数",
                dataIndex: "voteCount",
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
            // ebooks.value = [];
            axios
                .get("/ebook/list", {
                    params: {
                        page: params.page,
                        size: params.size,
                    },
                })
                .then((response) => {
                    loading.value = false;
                    const data = response.data;

                    ebooks.value = data.content.list;

                    // 重置分页按钮
                    pagination.value.current = params.page;
                    // pagination.value.total = data.content.total;
                    pagination.value.total = data.content.total;
                });
        };


        // -------- 表单 ---------
      /**
       * 数组，[100, 101]对应：前端开发 / Vue
       */
    //   const categoryIds = ref();
      const ebook = ref({});
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        modalLoading.value = true;
        // ebook.value.category1Id = categoryIds.value[0];
        // ebook.value.category2Id = categoryIds.value[1];
        axios.post("/ebook/save", ebook.value).then((response) => {
          
          const data = response.data; // data = commonResp
          if (data.success) {
            modalVisible.value = false;
            modalLoading.value = false;
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
        ebook.value = record;
        // ebook.value = Tool.copy(record);
        // categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
      };

        /**
       * 新增
       */
       const add = () => {
        modalVisible.value = true;
        ebook.value = {};

        // ebook.value = Tool.copy(record);
        // categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
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
                //  /ebook/list?page=1&size=4
                page: 1,
                size: pagination.value.pageSize,
            });
        });

        return {
            // param,
            ebooks,
            pagination,
            columns,
            loading,
            handleTableChange,

            edit,
            add,


            ebook,  
            modalLoading,
            modalVisible,
            handleModalOk,
        };
    },
});
</script>
