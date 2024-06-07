import { getBaseURL, get, post, deletes } from "./request";

const HttpManager = {
  // 获取图片信息
  attachImageUrl: (url: string) => url ? `${getBaseURL()}/${url}` : "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png",
  // =======================> 用户 API 完成
  // 登录
  signIn: ({username,password}) => post(`user/login/status`, {username,password}),
  signInByemail: ({email,password})=>post(`user/email/status`, {email,password}),
  // 注册
  SignUp: ({username,password,sex,phoneNum,email,birth,introduction,location}) => post(`user/add`, {username,password,sex,phoneNum,email,birth,introduction,location}),
  // 删除用户
  deleteUser: (id) => get(`user/delete?id=${id}`),
  // 更新用户信息
  updateUserMsg: ({id,username,sex,phoneNum,email,birth,introduction,location}) => post(`user/update`, {id,username,sex,phoneNum,email,birth,introduction,location}),
  updateUserPassword: ({id,username,oldPassword,password}) => post(`user/updatePassword`, {id,username,oldPassword,password}),
  // 返回指定ID的用户
  getUserOfId: (id) => get(`user/detail?id=${id}`),
  // 更新用户头像
  uploadUrl: (userId) => `${getBaseURL()}/user/avatar/update?id=${userId}`,



  getPlayHistory: (userId) => get(`playHistory/user?userId=${userId}`),
  // =======================> 歌单 API 完成
  // 获取全部歌单
  getSongList: () => get("songList"),
  // 获取歌单类型
  getSongListOfStyle: (style) => get(`songList/style/detail?style=${style}`),
  // 返回标题包含文字的歌单
  getSongListOfLikeTitle: (keywords) => get(`songList/likeTitle/detail?title=${keywords}`),
  // 返回歌单里指定歌单ID的歌曲
  getListSongOfSongId: (songListId) => get(`listSong/detail?songListId=${songListId}`),

  // =======================> 歌手 API  完成
  // 返回所有歌手
  getAllSinger: () => get("singer"),
  // 添加歌手
  setSinger: ({name, sex, birth, location, introduction}) => post(`singer/add`, {
    name,
    sex,
    birth,
    location,
    introduction
}),
  // 通过性别对歌手分类
  getSingerOfSex: (sex) => get(`singer/sex/detail?sex=${sex}`),
  // 根据用户名查找用户
  getUserLikeUsername: (username) => get(`user/likeUsername/detail?username=${username}`),
  // 关注歌手
  addFollow: ({followerId,followedId}) => post(`users/{id}/follow`,{followerId,followedId}),
  deleteFollow: (followerId,followedId) => get(`users/follow?followerId=${followerId}&&followedId=${followedId}`),
  //返回我关注的人
  myFollow: (followerId) => get(`users/follower?followerId=${followerId}`),
  //返回关注我的人
  followMe: (followedId) => get(`users/followed?followedId=${followedId}`),
  // =======================> 收藏 API 完成
  // 返回的指定用户ID的收藏列表
  getCollectionOfUser: (userId) => get(`collection/detail?userId=${userId}`),
  // 添加收藏的歌曲 type: 0 代表歌曲， 1 代表歌单S
  setCollection: ({userId,type,songId}) => post(`collection/add`,{userId,type,songId}),

  deleteCollection: (userId, songId) => deletes(`collection/delete?userId=${userId}&&songId=${songId}`),

  isCollection: ({userId, type, songId}) => post(`collection/status`, {userId, type, songId}),
  // =======================>歌曲 API 
  //根据id搜索歌曲信息
  songOfId: (id) => get(`song/detail?id=${id}`),
  // 更新歌曲信息
  updateSongMsg: ({id, singerId, name, introduction, lyric}) => post(`song/update`, {
    id,
    singerId,
    name,
    introduction,
    lyric
}),
  updateSongUrl: (id) => `${getBaseURL()}/song/url/update?id=${id}`,
  updateSongImg: (id) => `${getBaseURL()}/song/img/update?id=${id}`,
  updateSongLrc: (id) => `${getBaseURL()}/song/lrc/update?id=${id}`,
    // 删除歌曲
    deleteSong: (id) => deletes(`song/delete?id=${id}`),
  //返回所有用户的歌单
  allSongListConsumer: () => get("songListConsumer"),
  // 返回指定用户ID的收藏歌单列表
  songListConsumerOfUserId: (userId) => get(`songListConsumer/likeUserId/detail?userId=${userId}`),
// 创建新的歌单
  addSongListConsumer: ({title, userId, style, introduction  }) => post(`songListConsumer/add`, { title, userId, style, introduction }),
//标题搜索歌单
  songListConsumerOfLikeTitle: (title) => get(`songListConsumer/likeTitle/detail?title=${title}`),
//删除用户歌单
  deleteSongListConsumer: (id) => get(`songListConsumer/delete?id=${id}`),
  //更新歌单信息
  updateSongListConsumerMsg: ({title, userId, style, introduction, id  }) => post(`songListConsumer/update`,{ title, userId, style, introduction, id }),
  //更新歌单图片
  updateSongListConsumerPic: ({avatorFile,id}) => post(`songListConsumer/img/update`,{avatorFile,id}),

  isSongSheetCollection: ({ userId, type, songSheetId }) => post(`collection/songSheetStatus`, { userId, type, songSheetId }),
  //删除歌单里的歌曲
  deleteListSongConsumer: (songId,songListConsumerId) => get(`listSongConsumer/delete?songId=${songId}&&songListConsumerId=${songListConsumerId}`),
  // 返回歌单里指定歌单 ID 的歌曲
  listSongConsumerOfSongId: (songListConsumerId) => get(`listSongConsumer/detail?songListConsumerId=${songListConsumerId}`),
  //往歌单里面添加歌曲
  addListSongConsumer: ({songId,songListConsumerId}) => post(`listSongConsumer/add`,{songId,songListConsumerId}),
  //填入url和歌单id以更新歌单图片
  updateSongPicbyUrl: ({id,pic}) => post(`songListConsumer/update`,{id,pic}),
  // =======================> 评分 API 完成
  // 提交评分
  setRank: ({songListId,consumerId,score}) => post(`rankList/add`, {songListId,consumerId,score}),
  // 获取指定歌单的评分
  getRankOfSongListId: (songListId) => get(`rankList?songListId=${songListId}`),
  // 获取指定用户的歌单评分
  getUserRank: (consumerId, songListId) => get(`/rankList/user?consumerId=${consumerId}&songListId=${songListId}`),
  //根据歌单id找用户id
  searchUserBySongList: (SongListId) => get(`songListConsumer/byId/detail?id=${SongListId}`),

  // =======================> 评论 API 完成
  // 添加评论
  setComment: ({userId,content,songId,songListId,songListConsumerId,nowType}) => post(`comment/add`, {userId,content,songId,songListId,songListConsumerId,nowType}),
  // 删除评论
  deleteComment: (id) => get(`comment/delete?id=${id}`),
  // 点赞
  setSupport: ({id,up}) => post(`comment/like`, {id,up}),
  // 返回所有评论
  getAllComment: (type, id) => {
    let url = "";
    if (type === 1) {
      url = `comment/songList/detail?songListId=${id}`;
    } else if (type === 0) {
      url = `comment/song/detail?songId=${id}`;
    } else if (type === 2){
      url = `comment/songListConsumer/detail?songListConsumerId=${id}`;
    }
    return get(url);
  },

  // =======================> 歌曲 API
  // 返回指定歌曲ID的歌曲
  getSongOfId: (id) => get(`song/detail?id=${id}`),
  // 返回指定歌手ID的歌曲
  getSongOfSingerId: (id) => get(`song/singer/detail?singerId=${id}`),
  // 返回指定歌手名的歌曲
  getSongOfSingerName: (keywords) => get(`song/singerName/detail?name=${keywords}`),
  // 下载音乐
  downloadMusic: (url) => get(url, { responseType: "blob" }),

  //======================> 点赞api的优化 避免有些是重复的点赞！新增数据表了得

  testAlreadySupport:({commentId,userId}) => post(`userSupport/test`, {commentId,userId}),

  deleteUserSupport:({commentId,userId}) => post(`userSupport/delete`, {commentId,userId}),

  insertUserSupport:({commentId,userId}) => post(`userSupport/insert`, {commentId,userId}),

  //获取所有的海报
  getBannerList: () => get("banner/getAllBanner")
};



export { HttpManager };
