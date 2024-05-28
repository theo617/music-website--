<template>
  <div class="personal-song-list">
    <div class="header">
      <h2>我的歌单</h2>
      <el-button type="primary" @click="centerDialogVisible = true">创建歌单</el-button>
    </div>
    <div v-if="songLists.length > 0" class="song-list">
      <play-list :playList="data" path="personal-song-sheet-detail"></play-list> 
    </div>
    <div v-else class="no-data">暂无歌单</div>
    <!--添加歌单-->
  <el-dialog title="添加歌单" v-model="centerDialogVisible">
    <el-form label-width="70px" :model="newSongList">
      <el-form-item label="歌单名" prop="title">
        <el-input v-model="newSongList.title"></el-input>
      </el-form-item>
      <el-form-item label="歌单介绍" prop="introduction">
        <el-input v-model="newSongList.introduction"></el-input>
      </el-form-item>
      <el-form-item label="风格" prop="style">
        <el-input v-model="newSongList.style"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="createSongList">确 定</el-button>
      </span>
    </template>
  </el-dialog>
    <el-pagination
      class="pagination"
      background
      layout="total, prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="songLists.length"
      @current-change="handleCurrentChange"
    >
    </el-pagination>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed, reactive,getCurrentInstance } from "vue";
import { useStore } from "vuex";
import { HttpManager } from "@/api";
import PlayList from "@/components/PlayList.vue";
import axios from 'axios';

export default defineComponent({
  components: {
    PlayList,
  },
  setup() {
    const {proxy} = getCurrentInstance();
    const store = useStore();
    const songLists = ref([]);
    const userId = computed(() => store.getters.userId);
    const centerDialogVisible = ref(false); //控制弹出添加歌单对话框
    const pageSize = ref(15); // 页数
    const currentPage = ref(1); // 当前页
    const data = computed(() => songLists.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value));

    

    async function getsongListConsumerOfUserId(user_id) {
      songLists.value = ((await HttpManager.songListConsumerOfUserId(user_id)) as ResponseBody).data;
      currentPage.value = 1;
    }

    try {
      getsongListConsumerOfUserId(userId.value);
    } catch (error) {
      console.error(error);
    }
    // 获取当前页
    function handleCurrentChange(val) {
      currentPage.value = val;
    }

    //添加歌单逻辑
    const newSongList = reactive({
      title: "",
      introduction: "",
      user_id: "",
      style: "",
    });

    async function createSongList(){
      let title = newSongList.title;
      let introduction = newSongList.introduction;
      let style = newSongList.style;
      let user_id=userId.value;
      const result = (await HttpManager.addSongListConsumer({title, user_id,style,introduction})) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      
      if(result.success){
        getsongListConsumerOfUserId(userId.value);
        newSongList.title="";
        newSongList.introduction="";
        newSongList.style="";
      }
      centerDialogVisible.value=false;
    }



    return {
      songLists,
      newSongList,
      centerDialogVisible,
      getsongListConsumerOfUserId,
      createSongList,
      data,
      handleCurrentChange,
      pageSize,
      currentPage,
    };
  },
});
</script>

<style scoped>
.personal-song-list {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .no-data {
    text-align: center;
    color: grey;
    margin-top: 20px;
  }
}
</style>
