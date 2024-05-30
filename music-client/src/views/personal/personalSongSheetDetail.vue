<template>
  <el-container>
    <el-aside class="album-slide">
      <el-image class="album-img" fit="contain" :src="attachImageUrl(songDetails.pic)" />
      <h3 class="album-info">{{ songDetails.title }}</h3>
      <div class="creator-info">
        <el-image class="creator-img" fit="contain" :src="attachImageUrl(creator.avator)" @click="goToUserHome(creator.userId)" />
        <span>创建者：{{ creator.username }}</span>
      </div>
      <div class="button-group">
        <el-button v-if="collect" type="primary" @click="collectSongList">
          <Collection style="width:1em;height:1em; margin-right: 5px;" />
          收藏歌单
        </el-button>
        <template v-if="isCreator">
          <el-upload
            :action="uploadUrl(songDetails.id)"
            :show-file-list="false"
            :on-success="handleImgSuccess"
            :before-upload="beforeImgUpload"
          >
            <el-button type="primary">
              <Picture style="width:1em;height:1em; margin-right: 5px;" />
              更新图片
            </el-button>
          </el-upload>
          <el-button type="warning" @click="editDialogVisible = true" round>
            <EditPen style="width:1em;height:1em; margin-right: 5px;fill:aliceblue" />
            编辑歌单
          </el-button>
          <el-button type="danger" @click="delVisible=true" round>
            <Delete style="width:1em;height:1em; margin-right: 5px;" />
            删除歌单
          </el-button>
        </template>
      </div>
    </el-aside>
    <el-main class="album-main">
      <div class="album-header">
        <h1>{{ songDetails.title }}</h1>
        <p>{{ songDetails.introduction }}</p>
      </div>
      <!--歌曲-->
      <song-list class="album-body" :songList="currentSongList" :showDelete="isCreator" @deleteSong="deleteSong" :songListConsumerId="songDetails.id"></song-list>
      <comment :playId="songListId" :type="2"></comment>
    </el-main>
  </el-container>
  <el-dialog title="编辑歌单" v-model="editDialogVisible">
    <el-form label-width="70px" :model="editSongList">
      <el-form-item label="歌单名" prop="title">
        <el-input v-model="editSongList.title"></el-input>
      </el-form-item>
      <el-form-item label="歌单介绍" prop="introduction">
        <el-input v-model="editSongList.introduction"></el-input>
      </el-form-item>
      <el-form-item label="风格" prop="style">
        <el-input v-model="editSongList.style"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="editDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateSongList">确 定</el-button>
      </span>
    </template>
  </el-dialog>
  <!-- 删除提示框 -->
  <tsy-del-dialog :delVisible="delVisible" @confirm="deleteSongList" @cancelRow="delVisible = $event"></tsy-del-dialog>
</template>

<script lang="ts">
import { defineComponent, watch,ref, reactive,computed, getCurrentInstance } from "vue";
import { useStore } from "vuex";
import mixin from "@/mixins/mixin";
import SongList from "@/components/SongList.vue";
import Comment from "@/components/Comment.vue";
import tsyDelDialog from "@/components/dialog/tsyDelDialog.vue";
import { HttpManager } from "@/api";
import { Picture, Delete, Collection, EditPen } from "@element-plus/icons-vue";

export default defineComponent({
  components: {
    SongList,
    Comment,
    tsyDelDialog,
    Picture,
    Delete,
    Collection,
    EditPen,
  },
  setup() {
    const { proxy } = getCurrentInstance();
    const store = useStore();
    const { checkStatus } = mixin();
    const { routerManager } = mixin();

    const currentSongList = ref([]); // 存放的音乐
    const nowSongListId = ref(""); // 歌单 ID
    const assistText = ref("评价");
    const songDetails = computed(() => store.getters.songDetails); // 单个歌单信息
    const nowUserId = computed(() => store.getters.userId);
    const userId=ref("");
    nowSongListId.value = songDetails.value.id; // 给歌单ID赋值
    const dialogImgVisible = ref(false); //修改图片控件
    const editDialogVisible = ref(false);  //编辑控件
    const collect = ref(false); //判断是否显示收藏
    //歌单创建者信息
    const creator = reactive({
      username: '',
      avator: '',
      userId:'',
    });

    async function getCreatorInfo() {
      try {
        const listInfo = (await HttpManager.searchUserBySongList(nowSongListId.value)) as ResponseBody;
        if (listInfo.success && listInfo.data.length > 0) {
          const creat = listInfo.data[0].userId; 
          userId.value = creat;
          const result = (await HttpManager.getUserOfId(creat)) as ResponseBody;
          if (result.success && result.data.length > 0) {
            creator.username = result.data[0].username;
            creator.avator = result.data[0].avator;
            creator.userId = creat;
          }
        }
      } catch (error) {
        console.error("Error fetching creator info:", error);
      }
    }

    getCreatorInfo();
    //判断当前用户是否为歌单创建者
    const isCreator = ref(false);
    watch([nowUserId, userId], ([newNowUserId, newUserId]) => {
      if (checkStatus() && newNowUserId && newUserId) {
        isCreator.value = newNowUserId === newUserId;
      }
      if (checkStatus() ) {
        collect.value=true;
      }
    });

    
    //添加歌单逻辑
    const newSongList = reactive({
      title: songDetails.value.title,
      introduction: songDetails.value.introduction,
      user_id: userId.value,
      style: songDetails.value.style,
    });

    const editSongList = reactive({
      title: songDetails.value.title,
      introduction: songDetails.value.introduction,
      user_id: userId.value,
      style: songDetails.value.style,
    });
   
    // 收集歌单里面的歌曲
    async function getSongId(songListConsumerId) {
      const result = (await HttpManager.listSongConsumerOfSongId(songListConsumerId)) as ResponseBody;
      // 获取歌单里的歌曲信息
      for (const item of result.data) {
        const resultSong = (await HttpManager.getSongOfId(item.songId)) as ResponseBody;
        currentSongList.value.push(resultSong.data[0]);
      }
    }

    async function collectSongList() {
      // 收藏歌单的逻辑
      let title=newSongList.title;
      let introduction = newSongList.introduction;
      let style = newSongList.style;
      let user_id=userId.value;
      const result = (await HttpManager.addSongListConsumer({title, userId:user_id,style,introduction})) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
    if (result.success) {
    // 获取当前用户的所有歌单
    const songListResult = (await HttpManager.songListConsumerOfUserId(user_id)) as ResponseBody;
    const songLists = songListResult.data;
    // 找到 ID 最大的歌单，假设它是新创建的歌单
    const newSongList = songLists.reduce((max, songList) => (songList.id > max.id ? songList : max), songLists[0]);
    const newSongListId = newSongList.id;
    
    // 复制原歌单的歌曲到新歌单
    for (const song of currentSongList.value) {
      const addResult = (await HttpManager.addListSongConsumer({
        songId: song.id,
        songListConsumerId: newSongListId,
      })) as ResponseBody;

    }
    // 更新新歌单的图片
    await HttpManager.updateSongPicbyUrl({id: newSongListId, pic: songDetails.value.pic});
  }
  
    }

    const goToPage = (page) => {
      routerManager(page, { path: `/${page}` });
    };

    const delVisible = ref(false); // 显示删除框
    async function deleteSongList() {
      let id =songDetails.value.id;
      const result = await HttpManager.deleteSongListConsumer(id) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      if (result.success) {
        goToPage('personalSongList'); // 跳转到歌单列表界面
      }
      delVisible.value = false;
    }

    async function deleteSong(songId) {
      // 删除指定歌曲的逻辑
      // 示例接口调用：HttpManager.deleteSongFromList({ songId, songListId: nowSongListId.value })
    }

    //编辑歌单信息逻辑
    async function updateSongList() {
      let title = editSongList.title;
      let introduction = editSongList.introduction;
      let style = editSongList.style;
      let user_id = userId.value;
      let id = songDetails.value.id;
      const result = (await HttpManager.updateSongListConsumerMsg({ title, userId:user_id, style, introduction, id })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) {
        editDialogVisible.value = false;
        // 更新歌单详情
        songDetails.value.title = title;
        songDetails.value.introduction = introduction;
        songDetails.value.style = style;
      }
    }

    async function beforeImgUpload(file) {
      const ltCode = 2;
      const isLt2M = file.size / 1024 / 1024 < ltCode;
      const isExistFileType = ["jpg", "jpeg", "png", "gif"].includes(file.type.replace(/image\//, ""));

      if (!isExistFileType) {
        (proxy as any).$message.error(`图片只支持 ${["jpg", "jpeg", "png", "gif"].join("、")} 格式!`);
      }
      if (!isLt2M) {
        (proxy as any).$message.error(`上传图片大小不能超过 ${ltCode}MB!`);
      }

      return isExistFileType && isLt2M;
    }

    function uploadUrl(id) {
      return HttpManager.attachImageUrl(`/songListConsumer/img/update?id=${id}`);
    }
    //图片更新成功
    function handleImgSuccess(response, file) {
      (proxy as any).$message({
        message: response.message,
        type: response.type,
      });
      songDetails.value.pic = response.data;
    }
    getSongId(songDetails.value.id); // 获取歌单里面的歌曲ID
    //跳转到创建者主页
    const goToUserHome = (userId) => {
      routerManager('user-home', { path: `/user-home/${userId}` });
    };


    return {
      songDetails,
      assistText,
      currentSongList,
      songListId: nowSongListId,
      delVisible,
      attachImageUrl: HttpManager.attachImageUrl,
      collectSongList,
      deleteSongList,
      deleteSong,
      dialogImgVisible,
      editDialogVisible,
      editSongList,
      updateSongList,
      uploadUrl,
      handleImgSuccess,
      beforeImgUpload,
      goToUserHome,
      creator,
      Picture,
      Collection,
      EditPen,
      Delete,
      isCreator,
      collect,
      nowUserId,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";

.album-slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background: #fff;
  border-right: 1px solid #e0e0e0;

  .album-img {
    height: 250px;
    width: 250px;
    border-radius: 10%;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    cursor: pointer;
  }

  .album-info {
    width: 70%;
    padding-top: 2rem;
    text-align: center;
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
  }

  .creator-info {
    display: flex;
    align-items: center;
    margin-top: 10px;
    font-size: 1rem;
    color: #666;

    .creator-img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      margin-right: 10px;
    }
  }

  .button-group {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px;

    .el-button {
      margin-top: 10px;
      display: flex;
      align-items: center;
    }
  }
}

.album-main {
  padding: 20px;
  background: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .album-header {
    margin-bottom: 20px;

    h1 {
      font-size: 2rem;
      font-weight: bold;
      color: #333;
      margin: 0;
    }

    p {
      color: rgba(0, 0, 0, 0.6);
      margin: 10px 0 20px 0px;
      line-height: 1.6;
    }
  }

  .album-body {
    margin: 20px 0;
  }
}

@media screen and (min-width: $sm) {
  .album-slide {
    width: 350px;
  }
  .album-main {
    margin-left: 200px;
  }
}

@media screen and (max-width: $sm) {
  .album-slide {
    display: none;
  }
  .album-main {
    padding: 10px;
  }
}
</style>