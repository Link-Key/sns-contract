import Vue from 'vue'
import VueRouter from 'vue-router'
/**
 * 官方 component: () => import('../views/login/Login')
 * webpack懒加载  component: r => require.ensure([], () => r(require('../views/login/Login')), 'Login'),
 * import加载所有 import Login from '../views/login/Login'
 */

const User = () => import('../views/syscfg/userModule/user.vue')
const Robot = () => import('../views/syscfg/userModule/robot.vue')
const Contract = () => import('../views/syscfg/contract/contract.vue')
const Summary = () => import('../views/Summary.vue')
const Index = () => import('../views/Index.vue')
Vue.use(VueRouter)

const routes = [
    {
        path: '',
        name: 'Index',
        component: () => import('../views/Index'),
        meta: {
            keepAlive: false,
            title: '首页'
        }
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/login/Login'),
        meta: {
            keepAlive: false,
            title: '登录'
        }
    },
    {
        path: '*',//输错路由前往404
        redirect: '/404',
        meta: {
            keepAlive: false,
            title: '404'
        }
    },
    {
        path: '/404',
        name: 'notFound',
        component: () => import('../views/error/NotFound'),
        meta: {
            keepAlive: false,
            title: '404'
        }
    },
    {
        path: '/summary',
        name: 'home',
        redirect: '/summary',
        component: () => import('../views/home/Index'),
        meta: {
            keepAlive: false,
            title: '首页'
        },
        children: [
            {
                path: '/summary',
                name: 'summary',
                // route level code-splitting
                // this generates a separate chunk (about.[hash].js) for this route
                // which is lazy-loaded when the route is visited.
                component: Summary,
                meta: {//缓存、路由元信息、元字段
                    keepAlive: false,
                    title: '概述',
                    //requiresAuth: true
                }
            },
            {
                path: '/user',
                name: 'user',
                component: User,
                meta: {
                    keepAlive: false,
                    title: '用户'
                }
            },
            {
                path: '/robot',
                name: 'robot',
                component: Robot,
                meta: {
                    keepAlive: false,
                    title: '系统生产用户'
                }
            },
            {
                path: '/contract',
                name: 'contract',
                component: Contract,
                meta: {
                    keepAlive: false,
                    title: '合约'
                }
            },
        ]
    }
]

const router = new VueRouter({
    mode: 'history', // 去除默认的hash模式下,url会带有一个#
    base: process.env.BASE_URL,
    routes
})
export default router
