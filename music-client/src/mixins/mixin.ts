import { getCurrentInstance, computed,ref } from "vue";
import { useStore } from "vuex";
import { LocationQueryRaw } from "vue-router";
import { RouterName } from "@/enums";
import { HttpManager } from "@/api";
import axios from 'axios'
interface routerOptions {
  path?: string;
  query?: LocationQueryRaw;
}

export default function () {
  const { proxy } = getCurrentInstance();

  const store = useStore();
  const token = computed(() => store.getters.token);
  const uploadTypes = ref(["jpg", "jpeg", "png", "gif"]);
  function getUserSex(sex) {
    if (sex === 0) {
      return "女";
    } else if (sex === 1) {
      return "男";
    }
  }

  // 获取歌曲名
  function getSongTitle(str) {
    return str.split("-")[1];
  }

  // 获取歌手名
  function getSingerName(str) {
    return str.split("-")[0];
  }

  // 判断登录状态
  function checkStatus(status?: boolean) {
    if (!token.value) {
      if (status !== false)
        (proxy as any).$message({
          message: "请先登录",
          type: "warning",
        });
      return false;
    }
    return true;
  }

  // 播放
  function playMusic({ id, url, pic, index, name, lyric, currentSongList }) {
    const songTitle = getSongTitle(name);
    const singerName = getSingerName(name);
    const userId =  computed(() => store.getters.userId);
  

    proxy.$store.dispatch("playMusic", {
      id,
      url,
      pic,
      index,
      songTitle,
      singerName,
      lyric,
      currentSongList,
    });
  }

  function getFileName(path) {
    const parts = path.split('/');
    return parts[parts.length - 1];
  }

  // 下载
  async function downloadMusic({ songUrl, songName }) {
    if (!songUrl) {
      (proxy as any).$message({
        message: "下载链接为空！",
        type: "error",
      });
      console.error("下载链接为空！");
      return;
    }
    const fileName = getFileName(songUrl);
    const downUrl="/download/"+fileName
   // const result = (await HttpManager.downloadMusic(downUrl)) as ResponseBody;
   // console.log(result.data);

    // const eleLink = document.createElement("a");
    // eleLink.download = `${fileName}`;
    // eleLink.style.display = "none";
    // // 字符内容转变成 blob 地址
    // const blob = new Blob([result.data]);
    // console.log(blob)
    // eleLink.href = URL.createObjectURL(blob);
    // document.body.appendChild(eleLink); // 触发点击
    // eleLink.click();
    // document.body.removeChild(eleLink); // 移除

      const response = await axios.get(downUrl, {
        responseType: 'blob', // 指定响应类型为二进制数据
      });
      
      // 创建一个Blob URL来下载文件
      const blob = new Blob([response.data], { type: 'application/octet-stream' });
      const url = window.URL.createObjectURL(blob);

      // 创建一个隐藏的<a>标签来下载文件
      const link = document.createElement('a');
      link.href = url;
      link.download = fileName;
      document.body.appendChild(link);
      link.click();

      // 释放URL对象
      window.URL.revokeObjectURL(url);
      document.body.removeChild(link);

  }

  // 导航索引
  function changeIndex(value) {
    proxy.$store.commit("setActiveNavName", value);
  }
  function routerManager(routerName: string | number, options: { path: string; query?: any }) {
    try {
      console.log("routerManager called with:", routerName, options);
      if (typeof options.path !== 'string') {
        throw new Error(`Invalid path: ${options.path}`);
      }
      
      switch (routerName) {
        case RouterName.Search:
          proxy.$router.push({ path: options.path, query: options.query });
          break;
        default:
          proxy.$router.push({ path: options.path });
          break;
      }
    } catch (error) {
      console.error("Error in routerManager:", error);
    }
  }
  
  function beforeImgUpload(file) {
    const ltCode = 2;
    const isLt2M = file.size / 1024 / 1024 < ltCode;
    const isExistFileType = uploadTypes.value.includes(file.type.replace(/image\//, ""));

    if (!isExistFileType) {
      (proxy as any).$message.error(`图片只支持 ${uploadTypes.value.join("、")} 格式!`);
    }
    if (!isLt2M) {
      (proxy as any).$message.error(`上传头像图片大小不能超过 ${ltCode}MB!`);
    }
    
    return isExistFileType && isLt2M;
  }

  function beforeSongUpload(file) {
    const ltCode = 10;
    const isLt10M = file.size / 1024 / 1024 < ltCode;
    const testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
    const extension = testmsg === "mp3";

    if (!extension) {
      (proxy as any).$message({
        message: "上传文件只能是mp3格式！",
        type: "error",
      });
    }
    if (!isLt10M) {
      (proxy as any).$message.error(`上传头像图片大小不能超过 ${ltCode}MB!`);
    }

    return extension && isLt10M;
  }
  
  function beforeLrcUpload(file) {
    const ltCode = 1; // 1MB
    const isLt1M = file.size / 1024 / 1024 < ltCode;
    const extension = file.name.split('.').pop().toLowerCase() === "lrc";
  
    if (!extension) {
      (proxy as any).$message({
        message: "上传文件只能是lrc格式！",
        type: "error",
      });
    }
    if (!isLt1M) {
      (proxy as any).$message.error(`上传歌词文件大小不能超过 ${ltCode}MB!`);
    }
  
    return extension && isLt1M;
  }
  
  function goBack(step = -1) {
    proxy.$router.go(step);
  }

  return {
    getUserSex,
    getSongTitle,
    getSingerName,
    changeIndex,
    checkStatus,
    playMusic,
    routerManager,
    goBack,
    downloadMusic,
    beforeImgUpload,
    beforeSongUpload,
    beforeLrcUpload,
  };
}
