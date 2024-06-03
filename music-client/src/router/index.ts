import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
const routes: Array<RouteRecordRaw> = [
  {
    path: "/:pathMatch(.*)*",
    redirect: "/404",
  },
  {
    path: "/404",
    component: () => import("@/views/error/404.vue"),
  },
  {
    path: "/",
    name: "Tsy-container",
    component: () => import("@/views/TsyContainer.vue"),
    children: [
      {
        path: "/",
        name: "home",
        component: () => import("@/views/Home.vue"),
      },
      {
        path: "/sign-in",
        name: "sign-in",
        component: () => import("@/views/SignIn.vue"),
      },
      {
        path: "/sign",
        name: "sign",
        component: () => import("@/views/Sign.vue"),
      },
      {
        path: "/sign-up",
        name: "sign-up",
        component: () => import("@/views/SignUp.vue"),
      },
      {
        path: "/personal",
        name: "personal",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/views/personal/Personal.vue"),
      },
      {
        path: "/personalSongList",
        name: "personalSongList",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/views/personal/personalSongList.vue"),
      },
      {
        path: '/personalCollect',
        name: 'personalCollect',
        meta: {
          requireAuth: true,
        },
        component: () => import('@/views/personal/personalCollect.vue'),
      },
      {
        path: "/song-sheet",
        name: "song-sheet",
        component: () => import("@/views/song-sheet/SongSheet.vue"),
      },
      {
        path: "/user-song-list/:id",
        name: "user-song-list",
        component: () => import("@/views/user/UserSongList.vue"),
      },
      {
        path: "/song-sheet-detail/:id",
        name: "song-sheet-detail",
        component: () => import("@/views/song-sheet/SongSheetDetail.vue"),
      },
      {
        path: "/user-home/:id",
        name: "user-home",
        component: () => import("@/views/user/UserHome.vue"),
      },
      {
        path: "/user-song/:id",
        name: "user-song",
        component: () => import("@/views/user/UserSong.vue"),
      },
      {
        path: "/personal-song-sheet-detail/:id",
        name: "personal-song-sheet-detail",
        component: () => import("@/views/personal/personalSongSheetDetail.vue"),
      },
      {
        path: "/personal-song",
        name: "personal-song",
        component: () => import("@/views/personal/personalSong.vue"),
      },
      {
        path: "/singer",
        name: "singer",
        component: () => import("@/views/singer/Singer.vue"),
      },
      {
        path: "/singer-detail/:id",
        name: "singer-detail",
        component: () => import("@/views/singer/SingerDetail.vue"),
      },

      {
        path: "/lyric/:id",
        name: "lyric",
        component: () => import("@/views/Lyric.vue"),
      },
      {
        path: "/search",
        name: "search",
        component: () => import("@/views/search/Search.vue"),
      },
      {
        path: "/personal-data",
        name: "personal-data",
        component: () => import("@/views/setting/PersonalData.vue"),
      },
      {
        path: "/FPassword",
        name: "FPassword",
        component: ()=> import("@/views/FPassword.vue"),
      },
      {
        path: "/loginByemail",
        name: "loginByemail",
        component: ()=> import("@/views/loginByemail.vue"),
      },
      {
        path: "/weekly-report",
        name: "Weekly-Report",
        component: () => import("@/views/weekly-report/WeeklyReport.vue"),
      },
      {
        path: "/setting",
        name: "setting",
        meta: {
          requireAuth: true,
        },
        component: () => import("@/views/setting/Setting.vue"),
        children: [
          {
            path: "/setting/PersonalData",
            name: "personalData",
            meta: {
              requireAuth: true,
            },
            component: () => import("@/views/setting/PersonalData.vue"),
          }
        ]
      },
      
    ],
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
