/**
 * Created by ly-wangweiq on 2015/7/29.
 * * support mobile
 */
    //用户位置定位   使用geolocation定位
var mMap=function(){
      AMap.plugin('AMap.Geolocation', function () {
        var geolocation = new AMap.Geolocation({
          enableHighAccuracy: true,//是否使用高精度定位，默认:true
          timeout: 10000,          //超过10秒后停止定位，默认：5s
          buttonPosition: 'RB',    //定位按钮的停靠位置
          buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
          zoomToAccuracy: true,   //定位成功后是否自动调整地图视野到定位点

        });
        map.addControl(geolocation);
        geolocation.getCurrentPosition(function (status, result) {
          if (status == 'complete') {
            lnglat = [result.position.lng,result.position.lat];
            // 根据经纬度获取详细信息
            geocoder.getAddress(lnglat, function(status, result1) {
              if (status === 'complete'&&result1.regeocode) {
                address = result1.regeocode.formattedAddress;
                // document.getElementById('location').innerHTML = '地址：'+address;
                console.log(result1);
              }else{
                // document.getElementById('location').innerHTML = '地址查询错误';
                console.log('根据经纬度查询地址失败')
              }
            });
            // onComplete(result)
          } else {
            // onError(result)
          }
        });
      });

      //解析定位结果
      function onComplete(data) {
        document.getElementById('status').innerHTML = '定位成功'
        var str = [];
        str.push('经纬度：' + data.position);
        str.push('定位类别：' + data.location_type);
        if (data.accuracy) {
          str.push('精度：' + data.accuracy + ' 米');
        }//如为IP精确定位结果则没有精度信息
        str.push('是否经过偏移：' + (data.isConverted ? '是' : '否'));
        document.getElementById('result').innerHTML = str.join('<br>');
      }

      //解析定位错误信息
      function onError(data) {
        document.getElementById('status').innerHTML = '定位失败'
        document.getElementById('result').innerHTML = '失败原因排查信息:' + data.message;
      }

      // 构造点标记
      var marker = new AMap.Marker({
        map: map,               // 要显示该 marker 的地图对象
        icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
        position: new AMap.LngLat(lng,lat),      // 点标记在地图上显示的位置
        title: address,                         // 鼠标滑过点标记时的文字提示
        visible: true,                          // 点标记是否可见，默认值：true
        draggable: false,                        // 点标记是否可以拖拽移动,默认为false
        cursor: 'pointer'                       // 指定鼠标悬停时的鼠
      });


      // 将点标记添加到地图上
      map.add(marker);

      // 移除已创建的 marker
      //map.remove(marker);
    }();