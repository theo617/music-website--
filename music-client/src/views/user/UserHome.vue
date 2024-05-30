<template>
  <div class="user-home">
    <div class="user-header">
      <div class="user-img">
        <el-image class="image" fit="contain" :src="attachImageUrl(userPic)" />
      </div>
      <div class="user-info">
        <div class="username">{{ userInfo.username }}</div>
        <div class="introduction">{{ userInfo.introduction }}</div>
        <div class="user-stats">
          <span>关注 {{ userInfo.follow }}</span>
          <span>粉丝 {{ userInfo.fans }}</span>
        </div>
        <el-button class="follow-button" round @click="toggleFollow">
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
    const { routerManager } = mixin();
    const currentUserId = computed(() => store.getters.userId);
    const userPic = ref("");
    const isFollowing = ref(false);

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
        // Populate follow, fans, and activity counts as well
        userPic.value = data.avator;
      }
    };

    const fetchFollowedUsers = async () => {
      let id= route.params.id;
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

    /*const goToPage = (page) => {
      router.push({ path: `/${page}/${route.params.id}` });
    };*/

    onMounted(() => {
      const userId = route.params.id;
      getUserInfo(userId);
      fetchFollowedUsers();
    });


    return {
      userInfo,
      userPic,
      attachImageUrl: HttpManager.attachImageUrl,
      isFollowing,
      toggleFollow,
      goToPage,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.user-home {
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

  .user-header {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin-bottom: 60px;

    .user-img {
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

    .user-info {
      .username {
        font-size: 30px;
        font-weight: 600;
      }

      .introduction {
        font-size: 20px;
        margin: 10px 0;
      }

      .user-stats {
        display: flex;
        justify-content: space-around;
        width: 300px;
        margin-top: 10px;

        span {
          font-size: 16px;
        }
      }
    }

    .follow-button {
      margin-top: 20px;
    }
  }

  .user-nav {
    margin-bottom: 20px;
  }

  .user-content {
    padding: 0 10%;
  }
}
</style>
