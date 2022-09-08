<template>
  <el-row :gutter="24" style="height: 100%">
    <el-col :span="6">
      <el-col :span="24" style="height: calc((100% / 2) - 50px )">
        <el-card class="box-card" style="height: 100%">
          <div class="text item">
            <div class="label">{{ $t('dashboard.miners') }}</div>
            <div class="content">{{ data.minerStatistics.miners }}</div>
          </div>
          <div class="text item">
            <div class="label">{{ $t('dashboard.totalPowerLow') }}</div>
            <div class="content">{{ formatPower(data.minerStatistics.totalPowerLow) }}</div>
          </div>
          <div class="text item">
            <div class="label">{{ $t('dashboard.totalChargeVol') }}</div>
            <div class="content">{{ data.minerStatistics.totalChargeVol }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="24" style="margin-top: 15px;height: calc((100% / 2) - 65px )">
        <el-card class="box-card" style="height: 100%">
          <div class="text item">
            <div class="label">{{ $t('dashboard.uSDBmtMarketPrice') }}</div>
            <div class="content">${{ Number(data.blockchainstats.usdbmtMarketPrice).toFixed(2) }}</div>
          </div>
          <div class="text item">
            <div class="label">{{ $t('dashboard.totalBMTMarket') }}</div>
            <div class="content">${{ data.blockchainstats.totalBMTMarket }}</div>
          </div>
          <div class="text item">
            <div class="label">{{ $t('dashboard.tokenSupply') }}</div>
            <div class="content">{{ data.blockchainstats.tokenSupply }}/{{ data.blockchainstats.totalBMTMarket }}</div>
          </div>
        </el-card>
      </el-col>
    </el-col>
    <el-col :span="18">
      <div id="map" class="map"></div>
    </el-col>
  </el-row>
  <div v-show="data.minersDrawer" tabindex="-1" class="el-drawer__wrapper" style="z-index: 9999;">
    <div role="document" tabindex="-1" class="el-drawer__container el-drawer__open">
      <div tabindex="-1" class="el-drawer ltr" style="width: 450px;right: 0;margin-top: 58px">
        <header id="el-drawer__title" class="el-drawer__header">
          <span><i class="iconfont icon-miner_" style="color: black"></i>  Miners</span>
          <span style="cursor: pointer;" @click="data.minersDrawer = false"><i class="iconfont icon-shibai" style="color: black;color: #72767b;font-size: 30px"></i></span>
        </header>
        <div style="height: 1px;width: 100%;background-color: silver"></div>
        <section class="el-drawer__body">
          <el-collapse v-loading="data.minerInfoLoad" accordion>
            <el-collapse-item v-for="(miner,index) in data.minersList" :title="miner.address" :name="index">
              <el-row :gutter="20">
                <el-col :xs="12" :sm="8">Address:</el-col>
                <el-col :xs="12" :sm="16">
                  <router-link :to="'/miner/'+miner.address">{{miner.address}}</router-link>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="12" :sm="8">Miner Type:</el-col>
                <el-col :xs="12" :sm="16">
                  {{ miner.minerType ? Constant.MinerType[miner.minerType] : '' }}
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="12" :sm="8">Owner:</el-col>
                <el-col :xs="12" :sm="16">{{ miner.ownerAddress }}</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="12" :sm="8">Maker:</el-col>
                <el-col :xs="12" :sm="16">{{ miner.maker }}</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="12" :sm="8">Location:</el-col>
                <el-col :xs="12" :sm="16">
                  {{ miner.latitude + "," + miner.longitude }}
                  <i class="iconfont icon-weizhi" style="color: deepskyblue;cursor: pointer" @click="gotoMap(data.device.latitude,data.device.longitude)"/>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xs="12" :sm="8">Create Time:</el-col>
                <el-col :xs="12" :sm="16">{{ formatDate(miner.createTime, "yyyy-MM-dd hh:mm:ss") }}</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xl="24">Total Mining Revenue: {{ " "+formatToken(miner.earningMint) }}</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xl="24">Total Service Revenue: {{ " "+formatToken(miner.earningService) }}</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xl="24">Average power generation: {{ " "+formatPower(miner.power/1000) }}</el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :xl="24">
                  Total Power Generation: {{ " "+(miner.totalEnergyGeneration / 1000 / 1000).toFixed(3)+" kWh" }}
                </el-col>
              </el-row>
            </el-collapse-item>
          </el-collapse>
        </section>
      </div>
    </div>
  </div>
</template>

<script>
import {getBlockchainstats, getMinerStatistics,getMinerLocation,loadMinersInfo} from '@/api/statistics.js'
import {onMounted, reactive} from "vue";
import {toEther} from '@/utils/utils.js'
import {formatPower, formatElectricity, getTokenFixed, getAddress, formatDate,formatToken} from '@/utils/data_format.js'
import Constant from '@/utils/constant.js'
import "ol/ol.css"
import Map from "ol/Map"
import TileLayer from "ol/layer/Tile"
import VectorLayer from "ol/layer/Vector"
import VectorSource from "ol/source/Vector"
import XYZSource from "ol/source/XYZ"
import HexBinource from "ol-ext/source/HexBin"
import DayNight from "ol-ext/source/DayNight"
import Point from "ol/geom/Point"
import Feature from "ol/Feature"
import Style from "ol/style/Style"
import Fill from "ol/style/Fill"
import Circle from "ol/style/Circle"
import Stroke from "ol/style/Stroke"
import Select from "ol/interaction/Select"
import View from "ol/View"
export default {
  name: 'Dashboard',
  props: {
    msg: String
  },
  computed: {
    formatDate() {
      return formatDate
    },
    formatPower() {
      return formatPower
    },
    formatElectricity() {
      return formatElectricity
    },
    getTokenFixed() {
      return getTokenFixed
    },
    getAddress() {
      return getAddress
    },
    formatToken() {
      return formatToken
    },
    Constant() {
      return Constant
    }
  },
  setup() {

    const data = reactive({
      minersDrawer: false,
      minerInfoLoad: true,
      direction: 'rtl',
      minersList: null,
      blockchainstats: {
        usdbmtMarketPrice: 0,
        totalBMTMarket: 0,
        tokenSupply: 0
      },
      minerStatistics: {
        miners: 0,
        totalPowerLow: 0.0,
        totalChargeVol: 0.0
      },
      map: null,
      max: 5,
      min: 1,
    })

    const loadBlockchainstats = () => {
      getBlockchainstats().then((result) => {
        data.blockchainstats = result.data;
        let fixed1 = getTokenFixed(data.blockchainstats.tokenSupply);
        data.blockchainstats.tokenSupply = toEther(data.blockchainstats.tokenSupply, fixed1)
        let fixed2 = getTokenFixed(data.blockchainstats.tokenSupply);
        data.blockchainstats.totalBMTMarket = toEther(data.blockchainstats.totalBMTMarket, fixed2)
      }).catch((err) => {
        console.log(err);
      });
    }

    const loadMinerStatistics = () => {
      getMinerStatistics().then((result) => {
        console.log(result)
        data.minerStatistics = result.data;
        data.minerStatistics.totalChargeVol = (data.minerStatistics.totalChargeVol/1000 / 1000).toFixed(3)+" kWh"
      }).catch((err) => {
        console.log(err);
      });
    }

    const styleFn = function (f, res) {
      switch ("color") {
          /*case 'point':{
            var radius = Math.round(100000/res +0.5) * Math.min(1,f.get('features').length/max);
            if (radius < minRadius) radius = minRadius;
            return	[ new ol.style.Style({
              image: new ol.style.RegularShape({
                points: 6,
                radius: radius,
                fill: new ol.style.Fill({ color: [0,0,255] }),
                rotateWithView: true
              }),
              geometry: new ol.geom.Point(f.get('center'))
            })
              //, new ol.style.Style({ fill: new ol.style.Fill({color: [0,0,255,.1] }) })
            ];
          }
          case 'gradient': {
            var opacity = Math.min(1,f.get('features').length/max);
            return [ new ol.style.Style({ fill: new ol.style.Fill({ color: [0,0,255,opacity] }) }) ];
          }*/
        case 'color':
        default: {
          var color;
          if (f.get('features').length > data.max) color = [136, 0, 0, 1];
          else if (f.get('features').length > data.min) color = [255, 165, 0, 1];
          else color = [0, 136, 0, 1];
          return [new Style({fill: new Fill({color: color})})];
        }
      }
    };

    //创建六边形图层
    const createHexBinLayer = (source) => {
      const hexbin = new HexBinource({
        source: source,		// source of the bin
        size: 100000			// hexagon size (in map unit)
      });

      const vectorLayer = new VectorLayer({
        source: hexbin,
        opacity: 0.5,
        style: styleFn
      })

      return vectorLayer;
    }

    //创建marker图层
    const createMarkerLayer = () => {
      var markerSource = new VectorSource();
      addFeatures(markerSource);
      var markerLayer = new VectorLayer({source: markerSource, visible: true})
      return markerLayer;
    }

    //创建日照图层
    const createDayNightLayer = () => {
      var dayNightSource = new DayNight({ });
      var dayNightLayer=new VectorLayer({
        source: dayNightSource,
        style: new Style({
          image: new Circle({
            radius: 5,
            fill: new Fill({ color: 'red' })
          }),
          fill: new Fill({
            color: [0,0,50,.5]
          })
        })
      })

      return dayNightLayer;
    }

    //创建底图
    const createBaseLayer=()=>{
      var layers=[]
      var esriLayer=new TileLayer({
        source: new XYZSource({
          attributions:
              'Tiles © <a href="https://services.arcgisonline.com/ArcGIS/' +
              'rest/services/World_Topo_Map/MapServer">ArcGIS</a>',
          url:
              /*'https://server.arcgisonline.com/ArcGIS/rest/services/' +
              'World_Topo_Map/MapServer/tile/{z}/{y}/{x}',*/
              'https://wayback.maptiles.arcgis.com/arcgis/rest/services/world_imagery/wmts/1.0.0/default028mm/mapserver/tile/32645/{z}/{y}/{x}'
        }),
      })
      //layers.push(esriLayer)

      var world_Street_MapLayer=new TileLayer({
        source: new XYZSource({
          attributions:
              'Tiles © <a href="https://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer">ArcGIS</a>',
          url:
          /*'https://server.arcgisonline.com/ArcGIS/rest/services/' +
          'World_Topo_Map/MapServer/tile/{z}/{y}/{x}',*/
              'https://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}'
        }),
      })
      layers.push(world_Street_MapLayer)
      return layers
    }

    const initMap = () => {
      var defaultStyle = new Style({
        //边框样式
        stroke: new Stroke({
          color: 'rgba(30,144,255)',
          width: 2,
        }),
        //填充样式
        fill: new Fill({
          color: 'rgba(0, 0, 255, 0.1)',
        }),
        image: new Circle({
          radius: 5,
          fill: new Fill({
            color: 'white',
          })
        })
      })


      const baseLayers=createBaseLayer()
      data.map = new Map({
        layers: baseLayers,
        target: "map",
        view: new View({
          center: [0, 0],
          zoom: 2,
          /*projection: "EPSG:4326",
          center: [115.67724700667199, 37.73879478106912],
          zoom: 6,
          //maxZoom: 18,
          //minZoom: 1,*/
        })
      })



      //日照图层
      const dayNightLayer = createDayNightLayer();
      data.map.addLayer(dayNightLayer);

      //标注点图层
      var markerLayer = createMarkerLayer();
      data.map.addLayer(markerLayer);

      //六边形图层
      const hexBinLayerLayer = createHexBinLayer(markerLayer.getSource());
      data.map.addLayer(hexBinLayerLayer);


      //创建feature选择器
      var select = new Select({layers:[markerLayer,hexBinLayerLayer]});
      data.map.addInteraction(select);
      select.on('select', function (e) {
        if (e.selected.length) {
          const features = e.selected[0].get('features');
          if (features) {
            let minerAddressList = [];
            console.log(features)
            for (let i=0;i<features.length;i++){
              minerAddressList.push(features[i].values_.id);
            }
            //
            showMinerList(minerAddressList);
          } else {
            console.log("f---->"+features)
          }
        }
      });
    }

    const showMinerList = (minerAddressList) => {
      if(!data.minersDrawer){
        data.minersDrawer = true;
      }
      //
      data.minerInfoLoad = true;
      loadMinersInfo(minerAddressList).then((result) => {
        if(result.code == 0){
          data.minersList = result.data;
        }else {

        }
        data.minerInfoLoad = false
      }).catch((err) => {
        console.log(err);
      });
    }

    const addFeatures = (source) => {
      //
      let minerLocation = undefined;
      getMinerLocation().then((result) => {
        minerLocation = result.data
        //
        var features = [];
        for (const key in minerLocation) {
          let location = minerLocation[key]
          //
          //var f = new Feature(new Point([location.longitude, location.latitude]));
          //
          let x = (location.longitude * 20037508.34) / 180
          let y = Math.log(Math.tan(((90 + location.latitude) * Math.PI) / 360)) / (Math.PI / 180)
          y = (y * 20037508.34) / 180
          const f = new Feature(new Point([x, y]));
          //
          f.set('id', key);
          features.push(f);
        }
        source.clear(true);
        source.addFeatures(features);
        //
      }).catch((err) => {
        console.log(err);
      });
    }

    onMounted(() => {
      loadBlockchainstats()
      loadMinerStatistics()
      initMap();
    })
    return {
      data,
    }
  }
}
</script>
<style lang="scss" scoped>
.btn_xenon {
  text-align: center;
  cursor: pointer;
  background-color: aliceblue !important;
}
.btn_xenon:hover {
  background-color: rgb(83, 168, 255) !important;
}
.login_select {
  height: 45px;
  line-height: 45px;
  background-color: aliceblue;
  padding-right: 20px;
  padding-left: 6px;
  border-radius: 5px;
  margin-top: 10px;
  z-index: 99;
}

.el-drawer__open .el-drawer.ltr {
  animation: ltr-drawer-in .4s 0ms;
}

@keyframes ltr-drawer-in {
  0% {
    transform:translate(-100%)
  }
  to {
    transform:translate(0)
  }
}
@keyframes ltr-drawer-out {
  0% {
    transform:translate(0)
  }
  to {
    transform:translate(-100%)
  }
}

.el-drawer {
  position: absolute;
  box-sizing: border-box;
  background-color: #fff;
  display: flex;
}

.el-drawer__container {
  position: relative;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  height: 100%;
  width: 450px;
}

.el-drawer__header {
  align-items: center;
  color: #72767b;
  display: flex;
  margin-bottom: 2px !important;
  padding: var(--el-drawer-padding-primary);
  padding-bottom: 0;
}

.el-drawer__wrapper {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  margin: 0;
  width: 450px;
}

.wallet-img {
  font-size: 26px;
}

.wallet-img:hover {
  color: silver;
}
.box-card {
  .item {
    margin-bottom: 10px;
    .label {
      font-weight: bold;
      margin-bottom: 6px;
    }
  }
}

.map {
  width: 100%;
  height: calc(100% - 100px);
}
</style>
