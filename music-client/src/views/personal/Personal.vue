<template>
  <div class="personal">
    <div class="personal-header">
      <div class="personal-img" @click="dialogTableVisible = true">
        <el-image class="image" fit="contain" :src="attachImageUrl(userPic)"/>
      </div>
      <div class="personal-info">
        <div class="username">{{ personalInfo.username }}</div>
        <div class="introduction">{{ personalInfo.introduction }}</div>
        <div class="personal-stats">
          <span>关注 {{ personalInfo.follow }}</span>
          <span>粉丝 {{ personalInfo.fans }}</span>
          <span>动态 {{ personalInfo.activity }}</span>
        </div>
      </div>
      <el-button class="edit-info" round icon="el-icon-edit" @click="goPage()">修改个人信息</el-button>
    </div>
    <div class="personal-nav">
      <el-button @click="goToPage('personalSongList')">歌单</el-button>
      <el-button @click="goToPage('personalSong')">原创歌曲</el-button>
      <el-button @click="goToPage('personalMessage')">消息</el-button>
    </div>
    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch, reactive, onMounted } from "vue";
import { useStore } from "vuex";
import { HttpManager } from "@/api";
import mixin from "@/mixins/mixin";
import Upload from "../setting/Upload.vue";
import { RouterName } from "@/enums";

export default defineComponent({
  components: {
    Upload,
  },
  setup() {
    const store = useStore();
    const { routerManager } = mixin();
    const dialogTableVisible = ref(false);
    const currentTab = ref('PersonalSongList');
    const personalInfo = reactive({
      username: "",
      userSex: "",
      birth: "",
      location: "",
      introduction: "",
      follow: 0,
      fans: 0,
      activity: 0,
    });
    const userPic = computed(() => store.getters.userPic);
    const userId = computed(() => store.getters.userId);

    watch(userPic, () => {
      dialogTableVisible.value = false;
    });

    function goPage() {
      routerManager(RouterName.Setting, { path: RouterName.Setting });
    }

    const goToPage = (page) => {
      routerManager(page, { path: `/${page}` });
    };

    async function getUserInfo(id) {
      const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
      personalInfo.username = result.data[0].username;
      personalInfo.userSex = result.data[0].sex;
      personalInfo.birth = result.data[0].birth;
      personalInfo.introduction = result.data[0].introduction;
      personalInfo.location = result.data[0].location;
      // Populate follow, fans, and activity counts as well
    }
    
    

    onMounted(() => {
      getUserInfo(userId.value);
    });


    return {
      currentTab,
      personalInfo,
      userPic,
      dialogTableVisible,
      attachImageUrl: HttpManager.attachImageUrl,
      goPage,
      goToPage,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.personal {
  padding-top: $header-height + 150px;
  text-align: center;

  &::before {
    content: "";
    background-color: $color-blue-shallow;
    position: absolute;
    top: 0;
    width: 100%;
    height: $header-height + 150px;
  }

  .personal-header {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-bottom: 60px;

    .personal-img{
      height: 200px;
      width: 200px;
      border-radius: 50%;
      border: 5px solid $color-white;
      margin-bottom: 50px;
      cursor: pointer;

      .image {
        border-radius: 50%;
      }
    }

    .personal-info {
      .username {
        font-size: 30px;
        font-weight: 600;
      }

      .introduction {
        font-size: 20px;
        margin: 10px 0;
      }

      .personal-stats {
        display: flex;
        justify-content: space-around;
        width: 300px;
        margin-top: 10px;

        span {
          font-size: 16px;
        }
      }
    }

    .edit-info {
      margin-top: 20px;
    }
  }

  .personal-nav {
    margin-bottom: 20px;
  }

  .personal-content {
    padding: 0 10%;
  }
}
</style>