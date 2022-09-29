import { createWebHistory, createRouter } from "vue-router";

import Console from "@/views/console/index.vue";

const routes = [

    {
        path: "/",
        name: "ExplorerLayout",
        component: import('@/layout/explorer_layout.vue'),
        children: [
            {
                path: "",
                name: "Dashboard",
                component: import('@/views/Dashboard.vue'),
            },
            {
                path: "txs",
                name: "Transaction",
                component: import('@/views/transaction/index.vue'),
            },
            {
                path: "tx/:hash",
                name: "TransactionInfo",
                component: import('@/views/transaction/info.vue'),
            },
            {
                path: "/miners",
                name: "Miners",
                component: import('@/views/miners/index.vue'),
            },
            {
                path: "/miner/:address",
                name: "MinersInfo",
                component: import('@/views/miners/info.vue'),
            },
            {
                path: "/accounts",
                name: "Accounts",
                component: () => import('@/views/account/index.vue'),
            },
            {
                path: "/account/:address",
                name: "AccountInfo",
                component: () => import('@/views/account/info.vue'),
            },
            {
                path: "/blocks",
                name: "Blocks",
                component: () => import('@/views/block/index.vue'),
            }
        ]
    },
    {
        path: "/wallet",
        name: "WalletLayout",
        component: import('@/layout/wallet_layout.vue'),
        children: [
            {
                path: "",
                name: "My Dashboard",
                component: import('@/views/wallet/dashboard/dashboard.vue'),
            },
            {
                path: "apply",
                name: "Request a GameMiner",
                component: import('@/views/wallet/airdorp/apply.vue'),
            },
            {
                path: "claim/:address",
                name: "Claim GameMiner",
                component: import('@/views/wallet/airdorp/claim.vue'),
            },
            {
                path: "miners",
                name: "My Miners",
                component: import('@/views/wallet/miners/index.vue'),
            },
            {
                path: "miner/:address",
                name: "Miner Info",
                component: import('@/views/wallet/miners/info.vue'),
            },
            {
                path: "txs",
                name: "Transaction List",
                component: import('@/views/wallet/transaction/index.vue'),
            },
            {
                path: "withdraw",
                name: "Withdraw List",
                component: import('@/views/wallet/withdraw/index.vue'),
            },
            {
                path: "tx/:hash",
                name: "Transaction Info",
                component: import('@/views/wallet/transaction/info.vue'),
            },
        ]
    },
    {
        path: "/console/",
        name: "Console",
        component: Console,
        children:[
            {
                path: "miners",
                name: "OwnerMiners",
                component: () => import('@/views/console/miners.vue')
            },
            {
                path: "txs",
                name: "consoletxs",
                component: () => import('@/views/console/txs.vue')
            }
        ]
    },
    {
        path: "/tools/",
        name: "Tools",
        component: () => import('@/views/tools/index.vue'),
        children:[
            {
                path: "sign",
                name: "Sign",
                component: () => import('@/views/tools/sign.vue')
            },
            {
                path: "register",
                name: "MinerRegister",
                component: () => import('@/views/tools/register.vue')
            },
            {
                path: "onboard",
                name: "MinerOnboard",
                component: () => import('@/views/tools/onboard.vue')
            },
            {
                path: "virtualminer",
                name: "Virtualminer",
                component: () => import('@/views/tools/virtualminer.vue')
            }
        ]
    },
    {
        path: "/error",
        name: "error",
        component: import('@/views/error.vue')
    }
];

const router = createRouter({
    history: createWebHistory(),
    //history: createWebHashHistory(),
    routes,
});

router.beforeEach((to, from, next) => { //全局钩子函数
    to.matched.some((route) => {
        if (!window.ethereum && route.path !== '/error') {
            next('/error')
        }else if (window.ethereum && route.path === '/error') {
            next('/')
        } else {
            next();
        }
    });

});

export default router;
