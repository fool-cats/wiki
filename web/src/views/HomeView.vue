<template>
    <a-layout>
        <a-layout-sider width="200" style="background: #fff">
            <a-menu
                v-model:selectedKeys="selectedKeys2"
                v-model:openKeys="openKeys"
                mode="inline"
                :style="{ height: '100%', borderRight: 0 }"
                @click="handleClick"
            >
                <a-menu-item key="welcome">
                    <MailOutlined />
                    <span> 欢迎 </span>
                </a-menu-item>
                <!-- 主菜单/一级菜单 -->
                <a-sub-menu v-for="item in level1" :key="item.id">
                    <template v-slot:title>
                        <span>
                            <AppstoreOutlined />
                            {{ item.name }}
                        </span>
                    </template>
                    <!-- 二级菜单 -->
                    <a-menu-item v-for="child in item.children" :key="child.id">
                        <span>
                            <MailOutlined />
                            {{ child.name }}
                        </span>
                    </a-menu-item>
                </a-sub-menu>
            </a-menu>
        </a-layout-sider>
        <a-layout-content
            :style="{
                background: '#fff',
                padding: '24px',
                margin: 0,
                minHeight: '280px',
            }"
        >
            <div class="welcome" v-show="isShowWelcome">
                <h1>welcome to Java Wiki</h1>
            </div>
            <a-list
                item-layout="vertical"
                size="large"
                :grid="{ gutter: 16, column: 4 }"
                :data-source="ebooks"
                v-show="!isShowWelcome"
            >
                <template #renderItem="{ item }">
                    <a-list-item key="item.name">
                        <template #actions>
                            <span v-for="{ type, text } in actions" :key="type">
                                <component
                                    :is="type"
                                    style="margin-right: 8px"
                                />
                                {{ text }}
                            </span>
                        </template>
                        <a-list-item-meta :description="item.description">
                            <template #title>
                                <a :href="item.href">{{ item.name }}</a>
                            </template>
                            <template #avatar
                                ><a-avatar :src="item.cover"
                            /></template>
                        </a-list-item-meta>
                    </a-list-item>
                </template>
            </a-list>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive, toRef } from "vue";
import { message } from "ant-design-vue";
import axios from "axios";
import { Tool } from "@/utils/tool";
import {
    MailOutlined,
    QqOutlined,
    AppstoreOutlined,
    SettingOutlined,
} from "@ant-design/icons-vue";
// const listData: Record<string, string>[] = [];

// for (let i = 0; i < 23; i++) {
//     listData.push({
//         href: "https://www.antdv.com/",
//         title: `ant design vue part ${i}`,
//         avatar: "https://joeschmoe.io/api/v1/random",
//         description:
//             "Ant Design, a design language for background applications, is refined by Ant UED Team.",
//         content:
//             "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.",
//     });
// }

export default defineComponent({
    name: "HomeView",

    components: {
        MailOutlined,
        QqOutlined,
        AppstoreOutlined,
        SettingOutlined,
    },

    setup() {
        // console.log("HomeView setup");

        // 接收后端返回的数据
        const ebooks = ref();
        const ebooks1 = reactive({ books: [] });

        const isShowWelcome = ref(true);
        // 存放一级分类

        const level1 = ref();
        // 只在JS里使用不需要响应式，因为我们不在HTML里面使用。
        let categorys: any;
        /**
         * 数据查询
         * 查询所有分类数据，不分页。
         **/
        const handleQueryCategory = () => {
            // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
            // categorys.value = [];
            axios.get("/category/all").then((response) => {
                const data = response.data;

                if (data.success) {
                    categorys = data.content;
                    console.log("原始数据", categorys);

                    // 一级分类,存放数组

                    level1.value = [];
                    // 初始为，因为所有数据都是一级分类，一级分类的父id是000
                    level1.value = Tool.array2Tree(categorys, 0);

                    console.log("树形结构", level1);
                } else {
                    message.error(data.message);
                }
            });
        };


        const handleQueryEbook = () => {
            axios
                .get("/ebook/list", {
                    params: {
                        page: 1,
                        size: 1000,
                        categoryId2: categoryId2,
                    },
                })
                .then((response) => {
                    const data = response.data;
                    ebooks.value = data.content.list;
                    // ebooks1.books = data.content;
                    // console.log(response);
                });
        }

        let categoryId2 = 0;

        const handleClick = (value: any) => {
            // console.log("click ", value);
            if(value.key === "welcome") {
                isShowWelcome.value = true;
            } else {
                categoryId2 = value.key;
                isShowWelcome.value = false;
                handleQueryEbook();
            }
        };

   

        // 初始化逻辑尽量都放在生命周期函数里，setup就放一些参数，和方法的定义。
        onMounted(() => {
            // console.log("HomeView onMounted");
            handleQueryCategory();
            // handleQueryEbook();
        });

        const pagination = {
            onChange: (page: number) => {
                console.log(page);
            },
            pageSize: 3,
        };

        return {
            ebooks,
            ebooks2: toRef(ebooks1, "books"),
            // listData,
            pagination,
            // actions,
            level1,
            handleClick,

            isShowWelcome,
        };
    },
});
</script>

<style scoped>
.ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
}
</style>
