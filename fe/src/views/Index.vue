<template>
  <div class="hello" id="hello_id">
    <!-- <video-player class="video-player vjs-custom-skin"
            ref="videoPlayer"
            :playsinline="true"
            :options="playerOptions">
        </video-player> -->

    <video
      :style="fixStyle"
      autoplay
      loop
      muted
      class="fillWidth"
      v-on:canplay="canplay"
    >
      <source src="../assets/mp4/blackgrand.mp4" type="video/mp4" />
      <!-- 浏览器不支持 video 标签，建议升级浏览器。 -->
      <!-- <source src="../assets/video/G1w.webm" type="video/webm" />
      浏览器不支持 video 标签，建议升级浏览器。 -->
    </video>
    <div class="hello_top" id="top_id">
      <img
        id="comp-koxzfoezimgimage"
        alt="2.png"
        data-type="image"
        itemprop="image"
        src="../assets/images/logo.webp"
        style = "height:70px"
      />
      <!-- https://static.wixstatic.com/media/9485e3_062f42ba3cad45adb8b0f8a2fb631e1a~mv2.png/v1/fill/w_212,h_54,al_c,q_85,usm_0.66_1.00_0.01/2.webp -->
    </div>
    <el-button id="intoAdmin" class="intoAdmin" type="info" icon="el-icon-setting" @click="intoAdmin()" plain>admin</el-button>

    <div class="index_logo" id="logo_id">
      <h1>freeeagle.org</h1>
    </div>
    <div class="index_input" id="index_input">
      <input
        type="text"
        placeholder="Enter your address here"
        v-model="address"
      />
      <button @click="receive">Receive it!</button>
    </div>

    <div class="hello_bottom" id="address">
      合约地址： <a :href="contractUrl">{{ contractAddress }}</a>
    </div>

    <dialog-bar
      v-model="sendVal"
      type="confirm"
      :title="dialog_title"
      :content="dialog_content"
      :cancel="clickCancel()"
      @danger="clickDanger()"
      @confirm="clickConfirm()"
      dangerText="Delete"
    ></dialog-bar>
  </div>
</template>

<script>

import dialog from '../components/dialog/dialog.vue';
export default {
  name: "Index",

  components: {
    // Eject
    // popUp
    'dialog-bar': dialog,
  },

  data() {
    return {
      msg: "",
      vedioCanPlay: false,
      fixStyle: "",
      address: "",
      contractAddress: "",

      sendVal: false,
      dialog_title: "",
      dialog_content: "",
      contractUrl: "https://www.bscscan.com/address/",
    };
  },
  created() {
    this.getContractAddress();
  },
  methods: {
    canplay() {
      this.vedioCanPlay = true;
    },

    getContractAddress() {
      const _this = this;
      this.$api.officialWebsite.contract_find().then(res => {
        console.log(res)
        _this.contractAddress = res.data.data;
        _this.contractUrl = "https://www.bscscan.com/address/" + res.data.data;
      })
    },

    receive() {
      const _this = this;
      if (_this.address === "") {
        _this.dialog_content = "地址不能为空！！！"
        _this.openMask();
        return;
      }

      // let param = new URLSearchParams()
      // param.append('address', _this.address)

     this.$api.officialWebsite.account_add({'address': _this.address}).then(res => {
        // let code = res.code;
        let msg = res.data.msg;
        if (msg === "成功") {
          msg = "领取成功!!!";
        }
        _this.dialog_content = msg;
        _this.openMask();
      })

      // _this.$axios.post('/account/add',
      //   param
      // ).then(res=>{
      //
      // })

    },

    intoAdmin(){
      // console.log("进入管理系统")
      this.$router.push("/login");
    },

    openMask() {
      this.sendVal = true;
    },
    clickCancel() {
      console.log('点击了取消');
    },
    clickDanger() {
      console.log('这里是danger回调')
    },
    clickConfirm() {
      console.log('点击了confirm');
    }
  },
  mounted: function () {
    //屏幕自适应
    window.onresize = () => {
      const windowWidth = document.body.offsetWidth;
      const windowHeight = document.body.offsetHeight;
      const windowAspectRatio = windowHeight / windowWidth;
      let videoWidth;
      let videoHeight;
      if (windowAspectRatio < 0.5625) {
        videoWidth = windowWidth;
        videoHeight = videoWidth * 0.5625;
        this.fixStyle = {
          height: windowWidth * 0.5625 + "px",
          width: windowWidth + "px",
          "margin-bottom": (windowHeight - videoHeight) / 2 + "px",
          "margin-left": "initial",
        };
      } else {
        videoHeight = windowHeight;
        videoWidth = videoHeight / 0.5625;
        this.fixStyle = {
          height: windowHeight + "px",
          width: windowHeight / 0.5625 + "px",
          "margin-left": (windowWidth - videoWidth) / 2 + "px",
          "margin-bottom": "initial",
        };
      }
    };
    window.onresize();
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
@media only screen and (max-width: 780px) {
  #hello_id {
    width: 100%;
    height: 100vh;
    position: relative;
    overflow-x: hidden;
    overflow-y: hidden;
  }
  #top_id {
    width: 100%;
    height: 61px;
  }
  #top_id img {
    width: 165px;
  }

  #logo_id {
    width: 100%;
    height: 100px;
    position: absolute;
    top: 17%;
    text-align: center;
    left: 0px;
  }
  #logo_id h1 {
    color: #fff;
    font-size: 40px;
    letter-spacing: 0.05em;
    font-family: roboto-bold, roboto, sans-serif;
    font-weight: 800;
  }
  /* 输入框 */
  #index_input {
    width: 100%;
    height: 100px;
    position: absolute;
    top: 40%;
    left: 0px;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
  }
  #index_input input {
    width: 40%;
  }
  #index_input button {
    width: 40%;
    margin-top: 14px;
    margin-left: 0px;
  }
  #address {
    bottom: 10%;
  }
  #address a {
    color: #fff;
    text-decoration: none;
  }
  #intoAdmin {
    display: none;
  }
}
/* pc端 */

h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}

.homepage-hero-module,
.video-container {
  position: relative;
  height: 100vh;
  overflow: hidden;
}

.video-container .poster img {
  z-index: 0;
  position: absolute;
}

.video-container .filter {
  z-index: 1;
  position: absolute;
  background: rgba(0, 0, 0, 0.4);
  width: 100%;
}

.fillWidth {
  width: 100%;
}
/* logo */
.hello_top {
  width: 100%;
  height: 66px;
  background: #fff;
  position: absolute;
  top: 0px;
}
.hello {
  width: 100%;
  height: 100vh;
  overflow-x: hidden;
  overflow-y: hidden;
}

.logo_top img {
  width: 265px;
}
.index_logo {
  width: 100%;
  height: 100px;
  position: absolute;
  top: 17%;
  text-align: center;
}
.index_logo h1 {
  color: #fff;
  font-size: 71px;
  letter-spacing: 0.05em;
  font-family: roboto-bold, roboto, sans-serif;
  font-weight: 800;
}
.index_input {
  width: 100%;
  height: 100px;
  position: absolute;
  top: 40%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.index_input p {
  color: #fff;
  font-size: 12px;
  margin-left: 10px;
}

.index_input input {
  width: 26%;
  height: 33px;
  background: none;
  border: solid 1px #efefef;
  border-radius: 10px;
}
.index_input button {
  width: 8%;
  height: 33px;
  background: #fff;
  border: none;
  text-align: center;
  line-height: 33px;
  margin-left: 10px;
  border-radius: 10px;
}
.hello_bottom {
  width: 99%;
  height: 100px;
  position: absolute;
  bottom: 20%;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}
.hello_bottom a {
  color: #fff;
  text-decoration: none;
}

.intoAdmin{
    height: 40px;
    width: 90px;
    z-index: 10;
    position: absolute;
    top: 10px;
    right: 0px;
}
</style>
