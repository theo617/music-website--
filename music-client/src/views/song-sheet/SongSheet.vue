<template>
  <div class="song-sheet">
    <div class="header">
      <h1>歌单</h1>
    </div>
    <div class="content">
      <el-row :gutter="20">
        <el-col v-for="(sheet, index) in songSheets" :key="index" :span="6">
          <div class="sheet-card">
            <el-image :src="attachImageUrl(sheet.pic)" class="sheet-image" />
            <div class="sheet-info">
              <h2>{{ sheet.title }}</h2>
              <p>{{ sheet.description }}</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from "vue";
import { useStore } from "vuex";
import { HttpManager } from "@/api";

export default defineComponent({
  setup() {
    const store = useStore();
    const songSheets = computed(() => store.getters.songSheets);

    const attachImageUrl = (url: string) => {
      return HttpManager.attachImageUrl(url);
    };

    return {
      songSheets,
      attachImageUrl,
    };
  },
});
</script>

<style scoped>
.song-sheet {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;

  .header {
    text-align: center;
    margin-bottom: 30px;

    h1 {
      font-size: 36px;
      font-weight: bold;
      color: #333;
    }
  }

  .content {
    display: flex;
    justify-content: center;

    .sheet-card {
      background: #fff;
      border-radius: 10px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
      }

      .sheet-image {
        width: 100%;
        height: 200px;
        object-fit: cover;
      }

      .sheet-info {
        padding: 15px;

        h2 {
          font-size: 20px;
          font-weight: bold;
          margin: 0 0 10px;
        }

        p {
          font-size: 14px;
          color: #666;
          margin: 0;
        }
      }
    }
  }
}
</style>
