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
        title="文档表单"
        v-model:visible="modalVisible"
        :confirm-loading="modalLoading"
        @ok="handleModalOk"
    >
        <a-form
            :model="doc"
            :label-col="{ span: 6 }"
            :wrapper-col="{ span: 18 }"
        >
            <a-form-item label="名称">
                <a-input v-model:value="doc.name" />
            </a-form-item>
            <a-form-item label="名称">
                <a-tree-select
                    v-model:value="doc.parent"
                    style="width: 100%"
                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                    placeholder="选择父文档"
                    tree-default-expand-all
                    :tree-data="treeSelectData"
                    :replaceFields="{ title: 'name', key: 'id', value: 'id' }"
                >
                </a-tree-select>
            </a-form-item>
            <a-form-item label="顺序">
                <a-input v-model:value="doc.sort" type="textarea" />
            </a-form-item>
            <!-- 只展示一级文档-->
            <a-form-item label="父文档">
                <!-- <a-input v-model:value="doc.parent"></a-input> -->
                <a-select
                    ref="select"
                    v-model:value="doc.parent"
                    style="width: 120px"
                >
                    <a-select-option value="0">无</a-select-option>
                    <!--  -->
                    <a-select-option
                        v-for="c in level1"
                        :key="c.id"
                        :value="c.id"
                        :disabled="doc.id === c.id"
                    >
                        {{ c.name }}
                    </a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="内容">
                <div style="border: 1px solid #ccc">
                    <Toolbar
                        style="border-bottom: 1px solid #ccc"
                        :editor="editorRef"
                        :defaultConfig="toolbarConfig"
                        :mode="mode"
                    />
                    <Editor
                        style="height: 500px; overflow-y: hidden"
                        v-model="valueHtml"
                        :defaultConfig="editorConfig"
                        :mode="mode"
                        @onCreated="handleCreated"
                    />
                </div>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">

import { defineComponent, onMounted, ref,shallowRef,onBeforeUnmount } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/utils/tool";
import FacebookFilled from "@ant-design/icons-vue/lib/icons/FacebookFilled";
import { useRoute } from "vue-router";
import '@wangeditor/editor/dist/css/style.css' // 引入 css  
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'


export default defineComponent({
    name: "AdminDoc",
    methods: { FacebookFilled },
    components: { Editor, Toolbar },
    setup() {
        const route = useRoute();
        const param = ref();
        param.value = {};
        const docs = ref();
        // const pagination = ref({
        //     current: 1,
        //     pageSize: 10,
        //     total: 0,
        // });

          // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('<p>hello</p>')

        const toolbarConfig = {}
    const editorConfig = { placeholder: '请输入内容...' }

   // 组件销毁时，也及时销毁编辑器
   onBeforeUnmount(() => {
        const editor = editorRef.value
        if (editor == null) return
        editor.destroy()
    })

    const handleCreated = (editor:any) => {
      editorRef.value = editor // 记录 editor 实例，重要！
    }


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
            docs.value = [];
            axios.get("/doc/all").then((response) => {
                loading.value = false;
                const data = response.data;

                if (data.success) {
                    docs.value = data.content;
                    console.log("原始数据", docs.value);

                    // 一级分类,存放数组

                    level1.value = [];
                    // 初始为，因为所有数据都是一级分类，一级分类的父id是000
                    level1.value = Tool.array2Tree(docs.value, 0);

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
        //   const docIds = ref();
        // 因为树节点的属性状态，会随当前节点而变化，所以单独声明一个响应式变量。
        const treeSelectData = ref();
        treeSelectData.value = [];
        const doc = ref({});
        const modalVisible = ref(false);
        const modalLoading = ref(false);
        const handleModalOk = () => {
            modalLoading.value = true;
            // doc.value.doc1Id = docIds.value[0];
            // doc.value.doc2Id = docIds.value[1];
            axios.post("/doc/save", doc.value).then((response) => {
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
            // doc.value = record;
            doc.value = Tool.copy(record);
            // docIds.value = [doc.value.doc1Id, doc.value.doc2Id]

            // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
            treeSelectData.value = Tool.copy(level1.value);
            // 通过record获取当前id，然后调用setDisable方法，使得当前节点以及它的子节点全都disable。
            setDisable(treeSelectData.value, record.id);

            // 为选择树添加一个"无"
            treeSelectData.value.unshift({ id: 0, name: "无" });
        };

        /**
         * 新增
         */
        const add = () => {
            modalVisible.value = true;
            // 新增的时候获取id，因为新增的时候，id是空的，所以要获取id，必须在新增的时候获取。
            doc.value = {
                // 与route.query.ebookId绑定，当ebookId变化时，doc.value.ebookId也会变化
                ebookId: route.query.ebookId,
            };

            // 将level1的值复制到treeSelectData
            treeSelectData.value = Tool.copy(level1.value);

            //
            treeSelectData.value.unshift({ id: 0, name: "无" });

            // doc.value = Tool.copy(record);
            // docIds.value = [doc.value.doc1Id, doc.value.doc2Id]
        };

        /**
         * 删除
         */

        const handleDelete = (id: number) => {
            // 1. 获取要删除的整棵树和id，现在ids就有值来,使用join将数组转换为字符串。
            getDeleteIds(level1.value, id);
            axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
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

        const ids: Array<string> = [];

        // 查看整根树枝
        const getDeleteIds = (treeSelectData: any, id: any) => {
            // console.log(treeSelectData, id);
            // 遍历数组，即遍历某一层节点
            for (let i = 0; i < treeSelectData.length; i++) {
                const node = treeSelectData[i];
                if (node.id === id) {
                    // 如果当前节点就是目标节点
                    console.log("disabled", node);
                    // 将目标id放入结果集ids中
                    ids.push(node.id);
                    // 遍历所有子节点
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        for (let j = 0; j < children.length; j++) {
                            getDeleteIds(children, children[j].id);
                        }
                    }
                } else {
                    // 如果当前节点不是目标节点，则到其子节点再找找看。
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        getDeleteIds(children, id);
                    }
                }
            }
        };

        /**
         * 将某节点及其子孙节点全部置为disabled
         */
        const setDisable = (treeSelectData: any, id: any) => {
            // console.log(treeSelectData, id);
            // 遍历数组，即遍历某一层节点
            for (let i = 0; i < treeSelectData.length; i++) {
                const node = treeSelectData[i];
                if (node.id === id) {
                    // 如果当前节点就是目标节点
                    console.log("disabled", node);
                    // 将目标节点设置为disabled
                    node.disabled = true;

                    // 遍历所有子节点，将所有子节点全部都加上disabled
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        for (let j = 0; j < children.length; j++) {
                            setDisable(children, children[j].id);
                        }
                    }
                } else {
                    // 如果当前节点不是目标节点，则到其子节点再找找看。
                    const children = node.children;
                    if (Tool.isNotEmpty(children)) {
                        setDisable(children, id);
                    }
                }
            }
        };

        onMounted(() => {
            handleQuery();
            // console.log("test"+ doc.value);
        });

        return {
            param,
            // message,
            docs,
            columns,
            loading,
            level1,

            edit,
            add,
            handleDelete,

            doc,
            modalLoading,
            handleQuery,
            modalVisible,
            handleModalOk,
            treeSelectData,


            editorRef,
      valueHtml,
      mode: 'default', // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated
        };
    },
});
</script>
