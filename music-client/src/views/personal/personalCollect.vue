<template>
  <div class="personal">
    <div class="personal-header">
      <div class="personal-avatar" @click="dialogTableVisible = true">
        <el-image class="avatar-image" fit="cover" :src="attachImageUrl(userPic)" />
      </div>
      <div class="personal-info">
        <div class="username">{{ personalInfo.username }}喜欢的音乐</div>
      </div>
    </div>
    <div class="personal-body">
      <song-list :songList="collectSongList" :show="true" @changeData="changeData"></song-list>
    </div>
    <el-dialog v-model="dialogTableVisible" title="修改头像">
      <upload></upload>
    </el-dialog>
  </div>
</template>

  
  <script lang="ts">
  import { defineComponent, nextTick, ref, computed, watch, reactive } from "vue";
  import { useStore } from "vuex";
  import { Edit } from "@element-plus/icons-vue";
  import SongList from "@/components/SongList.vue";
  import mixin from "@/mixins/mixin";
  import { HttpManager } from "@/api";
  import { RouterName } from "@/enums";
  
  export default defineComponent({
    components: {
      SongList,
    },
    setup() {
      const store = useStore();
      const { routerManager } = mixin();
      const dialogTableVisible = ref(false);
      const collectSongList = ref([]); // 收藏的歌曲
      const personalInfo = reactive({
        username: "",
        userSex: "",
        birth: "",
        location: "",
        introduction: "",
      });
      const userId = computed(() => store.getters.userId);
      const userPic = computed(() => store.getters.userPic);
  
      watch(userPic, () => {
        dialogTableVisible.value = false;
      });
  
      function goPage() {
        routerManager(RouterName.Setting, { path: RouterName.Setting });
      }
  
      async function getUserInfo(id) {
        const result = (await HttpManager.getUserOfId(id)) as ResponseBody;
        personalInfo.username = result.data[0].username;
        personalInfo.userSex = result.data[0].sex;
        personalInfo.birth = result.data[0].birth;
        personalInfo.introduction = result.data[0].introduction;
        personalInfo.location = result.data[0].location;
      }
  
      // 获取收藏的歌曲
      async function getCollection(userId) {
        collectSongList.value = [];
        const result = (await HttpManager.getCollectionOfUser(userId)) as ResponseBody;
        const collectIDList = result.data || []; // 存放收藏的歌曲ID
        // 通过歌曲ID获取歌曲信息
        for (const item of collectIDList) {
          if (!item.songId) {
            console.error(`歌曲${item}异常`);
            continue;
          }
  
          const result = (await HttpManager.getSongOfId(item.songId)) as ResponseBody;
          collectSongList.value.push(result.data[0]);
        }
      }
  
      function changeData() {
        getCollection(userId.value);
      }
  
      nextTick(() => {
        getUserInfo(userId.value);
        getCollection(userId.value);
      });
  
      return {
        Edit,
        userPic,
        dialogTableVisible,
        collectSongList,
        personalInfo,
        attachImageUrl: HttpManager.attachImageUrl,
        goPage,
        changeData,
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

  .personal-header {
    position: relative;
    width: 100%;
    height: 300px;
    margin-bottom: 20px;
    background-image: url('@/assets/images/background.jpg');
    background-size: cover;
    background-position: center;
    overflow: hidden;

    .personal-avatar {
      position: absolute;
      top: 50px;
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
      top: 220px;
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
    }
  }

  .personal-body {
    padding: 0 10%;
  }
}
</style>
