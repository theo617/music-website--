<template>
  <div class="main">
    <el-dialog
      custom-class="play-history-dialog"
      title="播放记录"
      v-model="playHistoryVisible"
      :close-on-click-modal="false"
      :show-close="true"
      width="800px"
    >
      <div class="table-wrapper">
        <el-table :data="dataList" class="play-history-table">
          <el-table-column label="封面" width="80">
            <template v-slot="scope">
              <div class="song-info">
                <el-image
                  class="song-img"
                  fit="cover"
                  :src="attachImageUrl(scope.row.songPic)"
                  lazy
                />
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="songName" label="歌曲名" width="180">
            <template v-slot="scope">
              <div class="song-name">{{ scope.row.songName }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="singerName" label="歌手名" width="180">
            <template v-slot="scope">
              <div class="singer-name">{{ scope.row.singerName }}</div>
            </template>
          </el-table-column>
          <el-table-column prop="playTimeStamp" label="最近播放时间" :formatter="formatPlayTime" width="180">
          </el-table-column>
          <el-table-column prop="playCount" label="播放次数" width="180">
            <template v-slot="scope">
              <div class="play-count">{{ scope.row.playCount }}</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>
  
  <script lang="ts">
  import { defineComponent, ref, computed, onMounted } from "vue";
  import { useStore } from "vuex";
  import { HttpManager } from "@/api";
  import mixin from "@/mixins/mixin";
  import { formatDate } from "@/utils";
  
  export default defineComponent({
    name: "PlayHistory",
    props: {
      visible: {
        type: Boolean,
        required: true,
      },
      'onUpdate:visible': {
        type: Function,
        required: true,
      },
    },
    setup(props) {
      const store = useStore();
      const { playMusic, getSongTitle, getSingerName, checkStatus } = mixin();
  
      const playHistoryVisible = computed({
        get: () => props.visible,
        set: (value) => props['onUpdate:visible'](value),
      });
  
      const playHistory = ref([]);
      const dataList = ref([]);
      const inerList = ref([]);
      let intervalId = null;

      async function update() {
        if (checkStatus()) {
          const userId = store.getters.userId;
          const response = await (HttpManager.getPlayHistory(userId)) as ResponseBody;
          const historyData = response.data; 
          inerList.value = []; // 清空 dataList
  
          for (const item of historyData) {
            const songResponse = await (HttpManager.songOfId(item.songId)) as ResponseBody;
            const songData = songResponse.data[0];
          
            inerList.value.push({
              ...item,
              songName: getSongTitle(songData.name),
              singerName: getSingerName(songData.name) || '未知歌手',
              songPic: songData.pic,
              songUrl: songData.url,
            });
          }
          inerList.value.sort((a, b) => b.playTimeStamp - a.playTimeStamp);
          dataList.value=inerList.value;
          playHistory.value = dataList.value;
        }
      }
      onMounted(async () => {
        update();
        intervalId = setInterval(update, 1000); // 每5秒更新一次播放记录
      });
      
      const formatPlayTime = (row, column, cellValue) => {
      return formatDate(new Date(cellValue));
    };

  
      return {
        playHistoryVisible,
        playHistory,
        dataList,
        formatPlayTime,
        attachImageUrl: HttpManager.attachImageUrl,
      };
    },
  });
  </script>

<style lang="scss" scoped>

.play-history-dialog {
  display: flex;
  --el-dialog-header-height: 60px;
  --el-dialog-body-padding: 20px;
  overflow: hidden;
}
.table-wrapper {
  max-height: 600px; /* 设置表格最大高度 */
  overflow-y: auto; /* 添加垂直滚动条 */
}
.play-history-table {
  width: 100%;
  font-family: 'Helvetica Neue', Arial, sans-serif;
  font-size: 14px;
  color: #333;
  overflow-y: auto;
}

.song-info {
  display: flex;
  align-items: center;
}

.song-img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.song-name, .singer-name, .play-count {
  line-height: 50px;
  padding-left: 10px;
}

.el-dialog__header {
  background-color: #f5f5f5;
  border-bottom: 1px solid #ebeef5;
  color: #333;
  font-weight: bold;
  text-align: center;
}

.el-dialog__body {
  background-color: #fff;
}

.el-table__header-wrapper th {
  background-color: #fafafa;
  color: #606266;
  font-weight: bold;
}

.el-table__body-wrapper {
  background-color: #fff;
}

.el-table__row {
  transition: background-color 0.3s;
}

.el-table__row:hover {
  background-color: #f5f5f5;
}
</style>