<template>
  <div class="personal" >
    <div class="personal-header" :style="{ backgroundImage: `url(${selectedBackground})` }">
      <div class="personal-avatar" @click="dialogTableVisible = true">
        <el-image class="avatar-image" fit="cover" :src="attachImageUrl(userPic)" />
      </div>
      <div class="personal-info">
        <div class="username">{{ personalInfo.username }}</div>
        <div class="introduction">{{ personalInfo.introduction }}</div>
        <div class="personal-stats">
          <span>关注 {{ personalInfo.follow }}</span>
          <span>粉丝 {{ personalInfo.fans }}</span>
          <span>动态 {{ personalInfo.activity }}</span>
        </div>
        <el-button class="edit-info" round @click="goPage">修改个人信息</el-button>
        <el-button class="change-bg" round @click="dialogBgVisible = true">选择背景图片</el-button>
      </div>
    
      <div class="personal-nav">
        <el-button @click="goToPage('personalCollect')">❤ 我喜欢的音乐</el-button>
        <el-button @click="goToPage('personalSongList')">歌单</el-button>
        <el-button @click="goToPage('personal-Song')">原创歌曲</el-button>
        <el-button @click="goToPage('personalMessage')">消息</el-button>
      </div>
    </div>
    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
    <el-dialog v-model="dialogBgVisible" title="选择背景图片">
      <div class="bg-selection">
        <el-image
          v-for="(bg, index) in backgroundImages"
          :key="index"
          :src="bg"
          class="bg-image"
          @click="changeBackground(bg)"
        />
      </div>
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
    const dialogBgVisible = ref(false);
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
    const backgroundImages = [
    require('@/assets/images/background.jpg'),
      require('@/assets/images/lovesong.jpg'),
      require('@/assets/images/personal.png')
    ];
    const selectedBackground = ref(localStorage.getItem('selectedBackground') || backgroundImages[0]);

    watch(userPic, () => {
      dialogTableVisible.value = false;
    });

    function goPage() {
      routerManager(RouterName.Setting, { path: RouterName.Setting });
    }

    const goToPage = (page) => {
      routerManager(page, { path: `/${page}` });
    };

    function changeBackground(bg) {
      selectedBackground.value = bg;
      localStorage.setItem('selectedBackground', bg);
      dialogBgVisible.value = false;
    }

    async function getUserInfo(id) {
      const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
      personalInfo.username = result.data[0].username;
      personalInfo.userSex = result.data[0].sex;
      personalInfo.birth = result.data[0].birth;
      personalInfo.introduction = result.data[0].introduction;
      personalInfo.location = result.data[0].location;
      // Populate follow, fans, and activity counts as well
    }
    
    // 获取关注和粉丝数量
    async function getFollowInfo(id) {
      const followResult = (await HttpManager.myFollow(id)) as ResponseBody;
      const fansResult = (await HttpManager.followMe(id)) as ResponseBody;
      personalInfo.follow = followResult.data.length;
      personalInfo.fans = fansResult.data.length;
    }

    onMounted(() => {
      getUserInfo(userId.value);
      getFollowInfo(userId.value);
    });

    return {
      currentTab,
      personalInfo,
      userPic,
      dialogTableVisible,
      dialogBgVisible,
      backgroundImages,
      selectedBackground,
      attachImageUrl: HttpManager.attachImageUrl,
      goPage,
      goToPage,
      changeBackground,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.personal {
  position: relative;
  text-align: center;
  background-color: #fff;
  padding-bottom: 50px;
  background-size: cover;
  background-position: center;

  .personal-header {
    position: relative;
    width: 100%;
    height: 1020px;
    margin-bottom: 20px;
    background-size: cover;
    background-position: center;
    overflow: hidden;

    .personal-avatar {
      position: absolute;
      top: 220px;
      left: 50%;
      transform: translateX(-50%);
      height: 150px;
      width: 150px;
      border-radius: 50%;
      border: 5px solid $color-white;
      overflow: hidden;
      cursor: pointer;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);

      .avatar-image {
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
    }

    .personal-info {
      position: absolute;
      top: 400px;
      left: 50%;
      transform: translateX(-50%);
      background-color: rgba(255, 255, 255, 0.8);
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);

      .username {
        font-size: 26px;
        font-weight: bold;
        color: $color-black;
      }

      .introduction {
        font-size: 16px;
        color: $color-grey;
        margin: 10px 0;
      }

      .personal-stats {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin-top: 10px;

        span {
          font-size: 14px;
          color: $color-grey;
        }
      }

      .edit-info {
        margin-top: 20px;
        background-color: $color-blue;
        border-color: $color-blue;
        color: $color-white;
        &:hover {
          background-color: $color-blue-dark;
          border-color: $color-blue-dark;
        }
      }

      .change-bg {
        margin-top: 20px;
        background-color: $color-light-grey;
        border-color: $color-light-grey;
        color: $color-black;
        &:hover {
          background-color: $color-blue;
          border-color: $color-blue;
          color: $color-white;
        }
      }
    }
  }

  .personal-nav {
    position: absolute;
    top: 600px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    justify-content: center;
    gap: 10px;
    padding: 10px;
    .el-button {
      background-color: $color-light-grey;
      border-color: $color-light-grey;
      color: $color-black;
      &:hover {
        background-color: $color-blue;
        border-color: $color-blue;
        color: $color-white;
      }
    }
  }

  .bg-selection {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    .bg-image {
      width: 150px;
      height: 100px;
      cursor: pointer;
      border: 2px solid transparent;
      &:hover {
        border-color: $color-blue;
      }
    }
  }
}
</style>
