<template>
  <div class="user-home">
    <div class="user-header">
      <div class="user-avatar">
        <el-image class="avatar-image" fit="cover" :src="attachImageUrl(userPic)" />
      </div>
      <div class="user-info">
        <div class="username">{{ userInfo.username }}</div>
        <div class="introduction">{{ userInfo.introduction }}</div>
        <div class="user-stats">
          <span>关注 {{ userInfo.follow }}</span>
          <span>粉丝 {{ userInfo.fans }}</span>
        </div>
        <el-button v-if="isLoggedIn" class="follow-button" round @click="toggleFollow">
          <template v-if="isFollowing">
            <Check style="width:1em;height:1em; margin-right: 5px;" />
            已关注
          </template>
          <template v-else>
            <Plus style="width:1em;height:1em; margin-right: 5px;" />
            关注
          </template>
        </el-button>
      </div>
    </div>
    <div class="user-nav">
      <el-button @click="goToPage('user-song-list')">歌单</el-button>
      <el-button @click="goToPage('user-song')">原创歌曲</el-button>
    </div>
    <router-view></router-view>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, reactive, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { Plus, Check } from "@element-plus/icons-vue";

export default defineComponent({
  components: {
    Plus,
    Check,
  },
  setup() {
    const store = useStore();
    const route = useRoute();
    const router = useRouter();
    const { routerManager, checkStatus } = mixin();
    const currentUserId = computed(() => store.getters.userId);
    const userPic = ref("");
    const isFollowing = ref(false);

    const isLoggedIn = computed(() => checkStatus());

    const userInfo = reactive({
      username: "",
      sex: "",
      birth: "",
      location: "",
      introduction: "",
      follow: 0,
      fans: 0,
    });

    const getUserInfo = async (id) => {
      const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
      if (result.success && result.data.length > 0) {
        const data = result.data[0];
        userInfo.username = data.username;
        userInfo.sex = data.sex;
        userInfo.birth = data.birth;
        userInfo.location = data.location;
        userInfo.introduction = data.introduction;
        userPic.value = data.avator;
      }
    };

    const fetchFollowedUsers = async () => {
      if (!isLoggedIn.value) return;
      let id = route.params.id;
      const followResult = await HttpManager.myFollow(id) as ResponseBody;
      if (followResult.success) {
        userInfo.follow = followResult.data.length;
        isFollowing.value = followResult.data.some(user => user.followedId === Number(route.params.id));
      }

      const fansResult = await HttpManager.followMe(id) as ResponseBody;
      if (fansResult.success) {
        userInfo.fans = fansResult.data.length;
      }
    };

    const toggleFollow = async () => {
      const followerId = currentUserId.value;
      const followedId = route.params.id;
      if (isFollowing.value) {
        const result = await HttpManager.deleteFollow(followerId, followedId) as ResponseBody;
        if (result.success) {
          isFollowing.value = false;
          fetchFollowedUsers();
        }
      } else {
        const result = await HttpManager.addFollow({ followerId, followedId }) as ResponseBody;
        if (result.success) {
          isFollowing.value = true;
          fetchFollowedUsers();
        }
      }
    };

    const goToPage = (page) => {
      routerManager(page, { path: `/${page}/${route.params.id}` });
    };

    onMounted(() => {
      if(isLoggedIn.value){
      const userId = route.params.id;
      getUserInfo(userId);
      
        fetchFollowedUsers();}
    });

    return {
      userInfo,
      userPic,
      attachImageUrl: HttpManager.attachImageUrl,
      isFollowing,
      toggleFollow,
      goToPage,
      isLoggedIn,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.user-home {
  position: relative;
  text-align: center;
  background-color: #fff;
  padding-bottom: 50px;

  .user-header {
    position: relative;
    width: 100%;
    height: 1050px;
    margin-bottom: 0px;
    background-image: url('@/assets/images/background.jpg');
    background-size: cover;
    background-position: center;
    overflow: hidden;

    .user-avatar {
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

    .user-info {
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

      .user-stats {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin-top: 10px;

        span {
          font-size: 14px;
          color: $color-grey;
        }
      }

      .follow-button {
        margin-top: 20px;
        background-color: $color-blue;
        border-color: $color-blue;
        color: $color-white;
        &:hover {
          background-color: $color-blue-dark;
          border-color: $color-blue-dark;
        }
      }
    }
  }

  .user-nav {
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
}
</style>
