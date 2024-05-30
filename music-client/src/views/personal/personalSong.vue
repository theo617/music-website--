<template>
    <div class="personal-song">
      <div class="header">
        <el-breadcrumb class="crumbs" separator="/">
          <el-breadcrumb-item v-for="item in breadcrumbList" :key="item.name" :to="{ path: item.path, query: item.query }">
            {{ item.name }}
          </el-breadcrumb-item>
        </el-breadcrumb>
  
        <div class="handle-box">
          <el-input v-model="searchWord" placeholder="筛选关键词"></el-input>
          <el-button type="primary" @click="centerDialogVisible = true">添加歌曲</el-button>
        </div>
      </div>
  
      <div class="table-container">
        <el-table height="550px" border size="small" :data="data" @selection-change="handleSelectionChange">
          <el-table-column label="ID" prop="id" width="50" align="center"></el-table-column>
          <el-table-column label="歌曲图片" width="110" align="center">
            <template #default="scope">
              <div style="width: 80px; height: 80px; overflow: hidden">
                <img :src="attachImageUrl(scope.row.pic)" style="width: 100%" />
              </div>
              <div class="play" @click="setSongUrl(scope.row)">
                <svg class="icon" aria-hidden="true">
                  <use :xlink:href="toggle === scope.row.name ? playIcon : BOFANG"></use>
                </svg>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="歌名" prop="name" width="150"></el-table-column>
          <el-table-column label="专辑" prop="introduction" width="150"></el-table-column>
          <el-table-column label="歌词" align="center">
            <template #default="scope">
              <ul style="height: 100px; overflow: scroll">
                <li v-for="(item, index) in parseLyric(scope.row.lyric)" :key="index">
                  {{ item }}
                </li>
              </ul>
            </template>
          </el-table-column>
          <el-table-column label="资源更新" width="120" align="center">
            <template #default="scope">
              <el-upload :action="updateSongImg(scope.row.id)" :show-file-list="false" :on-success="handleImgSuccess" :before-upload="beforeImgUpload">
                <el-button>更新图片</el-button>
              </el-upload>
              <br />
              <el-upload :action="updateSongUrl(scope.row.id)" :show-file-list="false" :on-success="handleSongSuccess" :before-upload="beforeSongUpload">
                <el-button>更新歌曲</el-button>
              </el-upload>
              <br />
              <el-upload :action="updateSongLrc(scope.row.id)" :show-file-list="false" :on-success="handleSongSuccess" :before-upload="beforeLrcUpload">
                <el-button>更新歌词</el-button>
              </el-upload>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="160" align="center">
            <template #default="scope">
              <el-button @click="editRow(scope.row)">编辑</el-button>
              <el-button type="danger" @click="deleteRow(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
  
        <el-pagination
          class="pagination"
          background
          layout="total, prev, pager, next"
          :current-page="currentPage"
          :page-size="pageSize"
          :total="tableData.length"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>
  
      <el-dialog title="添加歌曲" v-model="centerDialogVisible">
        <el-form id="add-song" label-width="120px" :model="registerForm">
          <el-form-item label="歌曲名">
            <el-input type="text" name="name" v-model="registerForm.name"></el-input>
          </el-form-item>
          <el-form-item label="专辑">
            <el-input type="text" name="introduction" v-model="registerForm.introduction"></el-input>
          </el-form-item>
          <el-form-item label="歌词（有歌词lrc可以直接上传）">
            <el-input type="textarea" name="lyric" v-model="registerForm.lyric"></el-input>
          </el-form-item>
          <el-form-item label="歌词lrc上传">
            <input type="file" name="lrcfile"/>
          </el-form-item>
          <el-form-item label="歌曲上传">
            <input type="file" name="file" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="centerDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="addSong">确 定</el-button>
          </span>
        </template>
      </el-dialog>
  
      <el-dialog title="编辑" v-model="editVisible">
        <el-form :model="editForm">
          <el-form-item label="歌曲">
            <el-input v-model="editForm.name"></el-input>
          </el-form-item>
          <el-form-item label="专辑">
            <el-input v-model="editForm.introduction"></el-input>
          </el-form-item>
          <el-form-item label="歌词">
            <el-input type="textarea" v-model="editForm.lyric"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="editVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveEdit">确 定</el-button>
          </span>
        </template>
      </el-dialog>
  
      <tsy-del-dialog :delVisible="delVisible" @confirm="confirm" @cancelRow="delVisible = $event"></tsy-del-dialog>
  
      <el-dialog title="添加歌手" v-model="singerDialogVisible">
        <el-form label-width="80px" :model="singerForm" :rules="singerRules">
          <el-form-item label="性别" prop="sex">
            <el-radio-group v-model="singerForm.sex">
              <el-radio :label="0">女</el-radio>
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">保密</el-radio>
              <el-radio :label="2">组合</el-radio>
              <el-radio :label="3">不明</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="故乡" prop="location">
            <el-input v-model="singerForm.location"></el-input>
          </el-form-item>
          <el-form-item label="出生日期" prop="birth">
            <el-date-picker type="date" v-model="singerForm.birth"></el-date-picker>
          </el-form-item>
          <el-form-item label="歌手介绍" prop="introduction">
            <el-input type="textarea" v-model="singerForm.introduction"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="singerDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="addSinger">确 定</el-button>
          </span>
        </template>
      </el-dialog>
  
      <el-button v-if="!isSinger" type="primary" @click="singerDialogVisible = true">成为歌手</el-button>
    </div>
  </template>
  
  <script lang="ts">
  import { defineComponent, ref, getCurrentInstance, watch, reactive, computed,onMounted } from "vue";
  import { useStore } from "vuex";
  import { HttpManager } from "@/api";
  import mixin from "@/mixins/mixin";
  import tsyDelDialog from "@/components/dialog/tsyDelDialog.vue";
  import { parseLyric,getBirth } from "@/utils";
  import { Icon, RouterName } from "@/enums";


  
  export default defineComponent({
    components: {
      tsyDelDialog,
    },
    setup() {
      const { proxy } = getCurrentInstance();
      const store = useStore();
      const { routerManager, beforeImgUpload, beforeSongUpload, beforeLrcUpload } = mixin();
  
      const tableData = ref([]); // 记录歌曲，用于显示
      const tempDate = ref([]); // 记录歌曲，用于搜索时能临时记录一份歌曲列表
      const pageSize = ref(5); // 页数
      const currentPage = ref(1); // 当前页
      const isSinger = ref(false); // 是否为歌手
      const userId = computed(() => store.getters.userId);
      const singerId = ref("");
      const singerName = ref("");
      const toggle = ref(false); // 控制播放图标状态
      const BOFANG = ref(Icon.BOFANG);
      const ZANTING = ref(Icon.ZANTING);
      const breadcrumbList = computed(() => store.getters.breadcrumbList);
  
      const isPlay = computed(() => store.getters.isPlay); // 播放状态
      const playIcon = computed(() => (isPlay.value ? ZANTING.value : BOFANG.value)); // 播放状态
      // 计算当前表格中的数据
      const data = computed(() => {
        return tableData.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value);
      });
  
      const searchWord = ref(""); // 记录输入框输入的内容
      watch(searchWord, () => {
        if (searchWord.value === "") {
          tableData.value = tempDate.value;
        } else {
          tableData.value = [];
          for (let item of tempDate.value) {
            if (item.name.includes(searchWord.value)) {
              tableData.value.push(item);
            }
          }
        }
      });
  
      const centerDialogVisible = ref(false);
      const editVisible = ref(false);
      const delVisible = ref(false);
      const singerDialogVisible = ref(false);
  
      const singerForm = reactive({
        name: "",
        sex: "",
        birth: new Date(),
        location: "",
        introduction: "",
      });
  
      const singerRules = reactive({
        name: [{ required: true, trigger: "change" }],
        sex: [{ required: true, trigger: "change" }],
      });
  
      const registerForm = reactive({
        name: "",
        singerName: "",
        introduction: "",
        lyric: "",
      });
  
      const editForm = reactive({
        id: "",
        singerId: "",
        name: "",
        introduction: "",
        createTime: "",
        updateTime: "",
        pic: "",
        lyric: "",
        url: "",
      });
  
      const idx = ref(-1); // 记录当前要删除的行
      const multipleSelection = ref([]); // 记录当前要删除的列表
  
      // 获取歌曲
      async function getData() {
        tableData.value = [];
        tempDate.value = [];
        const result = (await HttpManager.getSongOfSingerId(singerId.value)) as ResponseBody;
        tableData.value = result.data;
        tempDate.value = result.data;
        currentPage.value = 1;
      }
  
      async function getSingerInfo() {
        const result = (await HttpManager.getAllSinger()) as ResponseBody;
        if (result.success) {
          const singerList = result.data;
          isSinger.value = singerList.some(singer => singer.name === store.getters.username);
          if (isSinger.value) {
            const singer = singerList.find(singer => singer.name === store.getters.username);
            singerId.value = singer.id;
            singerName.value = singer.name;
            getData();
          }
        }
      }
  
      function setSongUrl(row) {
        proxy.$store.commit("setUrl", row.url);
        toggle.value = row.name;
        if (isPlay.value) {
          proxy.$store.commit("setIsPlay", false);
        } else {
          proxy.$store.commit("setIsPlay", true);
        }
      }
  
      // 更新歌曲图片
      function updateSongImg(id) {
        return HttpManager.updateSongImg(id);
      }
      function updateSongUrl(id) {
        return HttpManager.updateSongUrl(id);
      }
      function updateSongLrc(id) {
        return HttpManager.updateSongLrc(id);
      }
  
      // 获取当前页
      function handleCurrentChange(val) {
        currentPage.value = val;
      }
  
      function handleSongSuccess(res) {
        (proxy as any).$message({
          message: res.message,
          type: res.type,
        });
        if (res.success) getData();
      }
  
      function handleLyricsSuccess(res) {
        (proxy as any).$message({
          message: res.message,
          type: res.type,
        });
        if (res.success) getData();
      }
  
      // 更新图片
      function handleImgSuccess(res, file) {
        (proxy as any).$message({
          message: res.message,
          type: res.type,
        });
        if (res.success) getData();
      }
  
      // 路由
      async function addSong() {
        const addSongForm = new FormData(document.getElementById("add-song") as HTMLFormElement);
        addSongForm.append("singerId", singerId.value);
        addSongForm.set("name", singerName.value + "-" + addSongForm.get("name"));
        if (!addSongForm.get("lyric")) addSongForm.set("lyric", "[00:00:00]暂无歌词");
  
        const req = new XMLHttpRequest();
        req.onreadystatechange = () => {
          if (req.readyState === 4 && req.status === 200) {
            let res = JSON.parse(req.response);
            (proxy as any).$message({
              message: res.message,
              type: res.type,
            });
            if (res.success) {
              getData();
              registerForm.name = "";
              registerForm.singerName = "";
              registerForm.introduction = "";
              registerForm.lyric = "";
            }
          }
        };
        req.open("post", HttpManager.attachImageUrl(`/song/add`), false);
        req.send(addSongForm);
        centerDialogVisible.value = false;
      }
  
      function editRow(row) {
        idx.value = row.id;
        editForm.id = row.id;
        editForm.singerId = row.singerId;
        editForm.name = row.name;
        editForm.introduction = row.introduction;
        editForm.createTime = row.createTime;
        editForm.updateTime = row.updateTime;
        editForm.pic = row.pic;
        editForm.lyric = row.lyric;
        editForm.url = row.url;
        editVisible.value = true;
      }
  
      async function saveEdit() {
        let id = editForm.id;
        let singerId = editForm.singerId;
        let name = editForm.name;
        let introduction = editForm.introduction;
        let lyric = editForm.lyric;
        const result = (await HttpManager.updateSongMsg({ id, singerId, name, introduction, lyric })) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });
        if (result.success) getData();
        editVisible.value = false;
      }
  
      async function confirm() {
        const result = (await HttpManager.deleteSong(idx.value)) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });
        if (result.success) getData();
        delVisible.value = false;
      }
  
      function deleteRow(id) {
        idx.value = id;
        delVisible.value = true;
      }
  
      function handleSelectionChange(val) {
        multipleSelection.value = val;
      }
  
      function deleteAll() {
        for (let item of multipleSelection.value) {
          deleteRow(item.id);
          confirm();
        }
        multipleSelection.value = [];
      }
      const username=computed(() => store.getters.username);
      async function addSinger() {
        let datetime = getBirth(singerForm.birth);
        let name = username.value;
        let sex = singerForm.sex;
        let birth = datetime;
        let location = singerForm.location;
        let introduction = singerForm.introduction;
  
        const result = (await HttpManager.setSinger({ name, sex, birth, location, introduction })) as ResponseBody;
        (proxy as any).$message({
          message: result.message,
          type: result.type,
        });
        if (result.success) {
          isSinger.value = true;
          getSingerInfo();
        }
      }

  
      onMounted(() => {
        getSingerInfo();
      });
  
      return {
        tableData,
        tempDate,
        pageSize,
        currentPage,
        data,
        searchWord,
        centerDialogVisible,
        registerForm,
        editVisible,
        editForm,
        delVisible,
        multipleSelection,
        singerDialogVisible,
        singerForm,
        singerRules,
        breadcrumbList,
        isSinger,
        playIcon,
        ZANTING,
        BOFANG,
        toggle,
        beforeImgUpload,
        beforeSongUpload,
        beforeLrcUpload,
        handleSelectionChange,
        handleCurrentChange,
        addSong,
        editRow,
        saveEdit,
        deleteRow,
        confirm,
        deleteAll,
        setSongUrl,
        parseLyric,
        updateSongImg,
        updateSongUrl,
        updateSongLrc,
        handleSongSuccess,
        handleLyricsSuccess,
        handleImgSuccess,
        addSinger,
        getBirth,
        attachImageUrl: HttpManager.attachImageUrl,
      };
    },
  });
  </script>
  
  <style scoped>
  .play {
    position: absolute;
    z-index: 100;
    width: 80px;
    height: 80px;
    top: 18px;
    left: 15px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }
  .icon {
    width: 2em;
    height: 2em;
    color: white;
    fill: currentColor;
    overflow: hidden;
  }
  .personal-song {
    padding: 20px;
  }
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  .table-container {
    margin-top: 20px;
  }
  </style>
  