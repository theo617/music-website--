<template>
  <div class="header">
    <!--图标-->
    <div class="header-logo" @click="goPage('/', 'home')">
      <Tsy-icon :icon="iconList.ERJI"></Tsy-icon>
      <span>{{ musicName }}</span>
    </div>
    <Tsy-header-nav class="Tsy-header-nav" :styleList="headerNavList" :activeName="activeNavName" @click="goPage"></Tsy-header-nav>
    <!--搜索框-->
    <div class="header-search">
      <el-input placeholder="搜索" :prefix-icon="Search" v-model="keywords" @keyup.enter="goSearch()" />
    </div>
    <!--登录/注册-->
    <div class="header-sign" v-if="!token">
      <Tsy-header-nav :styleList="signList" :activeName="activeNavName" @click="goPage"></Tsy-header-nav>
    </div>
    <!--用户信息-->
    <el-dropdown class="user-wrap" v-if="token" trigger="click">
      <el-image class="user" fit="contain" :src="attachImageUrl(userPic)" />
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="(item, index) in menuList" :key="index" @click.stop="goMenuList(item.path)">{{ item.name }}</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, getCurrentInstance, computed, reactive } from "vue";
import { Search } from "@element-plus/icons-vue";
import { useStore } from "vuex";
import TsyIcon from "./TsyIcon.vue";
import TsyHeaderNav from "./TsyHeaderNav.vue";
import mixin from "@/mixins/mixin";
import { HEADERNAVLIST, SIGNLIST, MENULIST, Icon, MUSICNAME, RouterName, NavName } from "@/enums";
import { HttpManager } from "@/api";

export default defineComponent({
  components: {
    TsyIcon,
    TsyHeaderNav,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { changeIndex, routerManager } = mixin();

    const musicName = ref(MUSICNAME);
    const headerNavList = ref(HEADERNAVLIST); // 左侧导航栏
    const signList = ref(SIGNLIST); // 右侧导航栏
    const menuList = ref(MENULIST); // 用户下拉菜单项
    const iconList = reactive({
      ERJI: Icon.ERJI,
    });
    const keywords = ref("");
    const activeNavName = computed(() => store.getters.activeNavName);
    const userPic = computed(() => store.getters.userPic);
    const token = computed(() => store.getters.token);

    function goPage(path: string, name: string) {
      if (!path && !name) {
        changeIndex(NavName.Home);
        routerManager(RouterName.Home, { path: RouterName.Home });
      } else {
        changeIndex(name);
        routerManager(path, { path });
      }
    }

    function goMenuList(path: string) {
      if (path == RouterName.SignOut) {
        proxy.$store.commit("setToken", false);
        changeIndex(NavName.Home);
        routerManager(RouterName.Home, { path: RouterName.Home });
      } else {
        routerManager(path, { path });
      }
    }

    function goSearch() {
      if (keywords.value !== "") {
        proxy.$store.commit("setSearchWord", keywords.value);
        routerManager(RouterName.Search, { path: RouterName.Search, query: { keywords: keywords.value } });
      } else {
        (proxy as any).$message({
          message: "搜索内容不能为空",
          type: "error",
        });
      }
    }

    return {
      musicName,
      headerNavList,
      signList,
      menuList,
      iconList,
      keywords,
      activeNavName,
      userPic,
      token,
      Search,
      goPage,
      goMenuList,
      goSearch,
      attachImageUrl: HttpManager.attachImageUrl,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 60px;
  line-height: 60px;
  padding: 0 30px;
  background-color: #242424;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 100;
}

.header-logo {
  display: flex;
  align-items: center;
  cursor: pointer;

  .icon {
    width: 50px;
    height: 50px;
    margin-right: 30px;
    fill:#f4ecec;
  }

  span {
    font-size: 25px;
    font-weight: bold;
    color: #f4ecec; //imusic颜色
  }
}

.header-search {
  flex: 1;
  display:flex;
  justify-content: center;
  margin: 0 20px;

  .el-input {
    width: 300px;
  }
}

.header-sign {
  display: flex;
  align-items: center;
  margin-right: 30px; /* 调整位置 */
}

.user-wrap {
  display: flex;
  align-items: left;
  margin-right: 30px; /* 调整位置 */

  .user {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right:60px;
  }
}
</style>
