<template>
  <el-row :gutter="24">
    <el-col :span="12">
      <el-card class="box-card">
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
          <div class="content">{{ formatElectricity(data.minerStatistics.totalChargeVol) }}</div>
        </div>

      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card class="box-card">
        <div class="text item">
          <div class="label">{{ $t('dashboard.uSDBmtMarketPrice') }}</div>
          <div class="content">{{ data.blockchainstats.usdbmtMarketPrice }}</div>
        </div>
        <div class="text item">
          <div class="label">{{ $t('dashboard.totalBMTMarket') }}</div>
          <div class="content">{{ data.blockchainstats.totalBMTMarket }}</div>
        </div>
        <div class="text item">
          <div class="label">{{ $t('dashboard.tokenSupply') }}</div>
          <div class="content">{{ data.blockchainstats.tokenSupply }}</div>
        </div>
      </el-card>
    </el-col>
  </el-row>
  <el-row>
    <div id="map" class="map"></div>
  </el-row>
</template>

<script>
import {getBlockchainstats, getMinerStatistics} from '@/api/statistics.js'
import {onMounted, reactive} from "vue";
import {toEther} from '@/utils/utils.js'
import {formatPower, formatElectricity} from '@/utils/data_format.js'

import "ol/ol.css"
import Map from "ol/Map"
import OSM from "ol/source/OSM"
import TileLayer from "ol/layer/Tile"
import VectorLayer from "ol/layer/Vector"
import VectorSource from "ol/source/Vector"
import HexBinource from "ol-ext/source/HexBin"
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
    formatPower() {
      return formatPower
    },
    formatElectricity() {
      return formatElectricity
    },
  },
  setup() {

    const data = reactive({
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
        data.blockchainstats.tokenSupply = toEther(data.blockchainstats.tokenSupply, 8)
        data.blockchainstats.totalBMTMarket = toEther(data.blockchainstats.totalBMTMarket, 8)
      }).catch((err) => {
        console.log(err);
      });
    }

    const loadMinerStatistics = () => {
      getMinerStatistics().then((result) => {
        console.log(result)
        data.minerStatistics = result.data;
        data.minerStatistics.totalChargeVol = (data.minerStatistics.totalChargeVol / 1000 / 1000).toFixed(3)
      }).catch((err) => {
        console.log(err);
      });
    }

    const addFeatures = (source, nb) => {
      var ssize = 20;		// seed size
      var ext = data.map.getView().calculateExtent(data.map.getSize());
      var dx = ext[2] - ext[0];
      var dy = ext[3] - ext[1];
      var dl = Math.min(dx, dy);
      var features = [];
      for (var i = 0; i < nb / ssize; ++i) {
        var seed = [ext[0] + dx * Math.random(), ext[1] + dy * Math.random()]
        for (var j = 0; j < ssize; j++) {
          var f = new Feature(new Point([
            seed[0] + dl / 10 * Math.random(),
            seed[1] + dl / 10 * Math.random()
          ]));
          f.set('id', i * ssize + j);
          features.push(f);
        }
      }
      source.clear(true);
      source.addFeatures(features);
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

    const createHexBin = (source) => {
      const hexbin = new HexBinource({
        source: source,		// source of the bin
        size: 100000			// hexagon size (in map unit)
      });

      const vectorLayer = new VectorLayer({
        source: hexbin,
        opacity: 0.5,
        style: styleFn
      })

      data.map.addLayer(vectorLayer);
    }

    const initMap = () => {
      //


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
      data.map = new Map({
        layers: [
          new TileLayer({
            source: new OSM(),
            style: defaultStyle
          })
        ],
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

      var select = new Select();
      data.map.addInteraction(select);
      select.on('select', function (e) {
        if (e.selected.length) {
          var f = e.selected[0].get('features');
          if (f) {
            console.log(e.selected[0].get('features').length)
          } else {
            console.log(0)
          }
        } else {
          console.log(0)
        }
      });

      var source = new VectorSource();
      addFeatures(source, 2000);

      var layerSource = new VectorLayer({source: source, visible: true})
      data.map.addLayer(layerSource);

      createHexBin(source);
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
.box-card {
  .item {
    margin-bottom: 20px;

    .label {
      font-weight: bold;
      margin-bottom: 6px;
    }
  }

}

.map {
  width: 100%;
  height: 400px;
  margin-top: 20px;
}
</style>
