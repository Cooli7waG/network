import { createWebHistory, createRouter } from "vue-router";
import Dashboard from "@/views/Dashboard.vue";
import Transaction from "@/views/transaction/index.vue";
import TransactionInfo from "@/views/transaction/info.vue";
import Miners from "@/views/miners/index.vue";
import MinersInfo from "@/views/miners/info.vue";

import Console from "@/views/console/index.vue";

const routes = [
    {
        path: "/",
        name: "Dashboard",
        component: Dashboard,
    },
    {
        path: "/txs",
        name: "Transaction",
        component: Transaction,
    },
    {
        path: "/tx/:hash",
        name: "TransactionInfo",
        component: TransactionInfo,
    },
    {
        path: "/miners",
        name: "Miners",
        component: Miners,
    },
    {
        path: "/miner/:address",
        name: "MinersInfo",
        component: MinersInfo,
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
    }
];

const router = createRouter({
    history: createWebHistory(),
    //history: createWebHashHistory(),
    routes,
});

export default router;
