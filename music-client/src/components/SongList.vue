<template>
  <div class="content">
    <el-table highlight-current-row :data="dataList" @row-click="handleClick">
      <el-table-column prop="songName" label="歌曲" />
      <el-table-column prop="singerName" label="歌手" />
      <el-table-column prop="introduction" label="专辑" />
      <el-table-column label="编辑" width="80" align="center">
        <template #default="scope">
          <el-dropdown>
            <el-icon @click="handleEdit(scope.row)"><MoreFilled /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  :icon="Download"
                  @click="
                    downloadMusic({
                      songUrl: scope.row.url,
                      songName: scope.row.name,
                    })
                  ">下载</el-dropdown-item>
                <el-dropdown-item :icon="Delete" v-if="showDelete" @click="deleteCollection(scope.row)">删除</el-dropdown-item>
                <el-dropdown-item :icon="StarFilled" @click="showCollectDialog(scope.row)">收藏到歌单</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="收藏到歌单" v-model="collectDialogVisible">
      <el-form label-width="70px">
        <el-form-item label="歌单">
          <el-select v-model="selectedSongListId" placeholder="选择歌单">
            <el-option
              v-for="item in userSongLists"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="collectDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="collectSong">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, getCurrentInstance, toRefs, computed, reactive, ref } from "vue";
import { useStore } from "vuex";
import { MoreFilled, Delete, Download, StarFilled } from "@element-plus/icons-vue";

import mixin from "@/mixins/mixin";
import { HttpManager } from "@/api";
import { Icon } from "@/enums";

export default defineComponent({
  components: {
    MoreFilled,
  },
  props: {
    songList: Array,
    songListConsumerId: {
      type: Number,
      required: false, // 设置为非必须
      default: 0, // 设置默认值
    },
    showDelete: {
      type:Boolean,
      default: false
    }
  },
  emits: ["changeData"],
  setup(props) {
    const { getSongTitle, getSingerName, playMusic, checkStatus, downloadMusic } = mixin();
    const { proxy } = getCurrentInstance();
    const store = useStore();

    const { songList } = toRefs(props);

    const iconList = reactive({
      dislike: Icon.Dislike,
      like: Icon.Like,
    });

    const songUrl = computed(() => store.getters.songUrl);
    const singerName = computed(() => store.getters.singerName);
    const songTitle = computed(() => store.getters.songTitle);
    const dataList = computed(() => {
      const list = [];
      songList.value.forEach((item: any, index) => {
        item["songName"] = getSongTitle(item.name);
        item["singerName"] = getSingerName(item.name);
        item["index"] = index;
        list.push(item);
      });
      return list;
    });

    const collectDialogVisible = ref(false);
    const selectedSong = ref(null);
    const userSongLists = ref([]);
    const selectedSongListId = ref(null);


    async function showCollectDialog(song) {
      selectedSong.value = song;
      collectDialogVisible.value = true;
      const result = (await HttpManager.songListConsumerOfUserId(store.getters.userId)) as ResponseBody;
      userSongLists.value = result.data;
    }

    //收藏到指定歌单
    async function collectSong() {
      const result = (await HttpManager.addListSongConsumer({
        songId: selectedSong.value.id,
        songListConsumerId: selectedSongListId.value,
      })) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });
      collectDialogVisible.value = false;
    }

    function handleClick(row) {
      playMusic({
        id: row.id,
        url: row.url,
        pic: row.pic,
        index: row.index,
        name: row.name,
        lyric: row.lyric,
        currentSongList: songList.value,
      });
    }

    function handleEdit(row) {
      console.log("row", row);
    }

    const userId = computed(() => store.getters.userId);

    async function deleteCollection(row) {
      if (!checkStatus()) return;

      const result = (await HttpManager.deleteListSongConsumer(row.id, props.songListConsumerId)) as ResponseBody;
      (proxy as any).$message({
        message: result.message,
        type: result.type,
      });

      if (result.success) proxy.$emit("changeData", result.data);
    }

    return {
      dataList,
      iconList,
      Delete,
      StarFilled,
      Download,
      songUrl,
      singerName,
      songTitle,
      handleClick,
      handleEdit,
      downloadMusic,
      deleteCollection,
      collectDialogVisible,
      showCollectDialog,
      collectSong,
      userSongLists,
      selectedSongListId,
    };
  },
});
</script>

<style lang="scss" scoped>
@import "@/assets/css/var.scss";
@import "@/assets/css/global.scss";

.content {
  background-color: $color-white;
  border-radius: $border-radius-songlist;
  padding: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  .el-table__row.current-row {
    color: $color-black;
    font-weight: bold;
    background-color: #f5f5f5 !important;
  }

  .el-table__row {
    cursor: pointer;
    &:hover {
      background-color: #f5f5f5 !important;
    }
  }

  .el-table-column--align-center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .el-icon {
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;

    &:hover {
      transform: scale(1.2);
      box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
    }
  }
}
</style>
