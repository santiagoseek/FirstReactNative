/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View,

  TouchableOpacity,
    Navigator,
    Image,
    TouchableHighlight,
    NativeModules,
    ScrollView,
    Dimensions
} from 'react-native';

import CTModuleManager from './jsAndroid/CTModuleManager';
//import ToastAndroid from './jsAndroid/ToastAndroid';
//import CallNative from './jsAndroid/CallNative';
//
//
//class firstRNProject extends Component {
//  render() {
//    return (
//      <View style={styles.container}>
//        <Text style={styles.welcome}>
//          Welcome to React Native!
//        </Text>
//        <Text style={styles.instructions}>
//          To get started, edit index.android.js
//        </Text>
//        <Text style={styles.instructions}>
//          Shake or press menu button for dev menu
//        </Text>
//
//        <Text onPress={this.buttonClick} style={styles.button}>
//          Click to invoke the Button click event.
//        </Text>
//      </View>
//    );
//  }
//
//  buttonClick(event){
//     var data = {
//       "url":"",
//       "projectName":""
//     }
//     console.log("this is test js.");
//     ToastAndroid.show('this is toast test',ToastAndroid.LONG);
//     CallNative.callNative("startActivity","goToHome",data);
//
//  }
//}
//
//const styles = StyleSheet.create({
//  container: {
//    flex: 1,
//    justifyContent: 'center',
//    alignItems: 'center',
//    backgroundColor: '#F5FCFF',
//  },
//  welcome: {
//    fontSize: 20,
//    textAlign: 'center',
//    margin: 10,
//  },
//  instructions: {
//    textAlign: 'center',
//    color: '#333333',
//    marginBottom: 5,
//  },
//});
//
//AppRegistry.registerComponent('firstRNProject', () => firstRNProject);


var {height, width} = Dimensions.get('window');

var chartList = [
	{
		"icon": require('./kpi.png'),
    "id":"kpi",
    "name":"KPI"
	},
  {
		"icon": require('./default.png'),
    "id":"cat",
    "name":"Cat"
	},
  {
		"icon": require('./default.png'),
    "id":"other",
    "name":"Other"
	}
]


var coldcat = React.createClass({
  onPress:function(id){
    console.log(id);
      switch (id) {
        case 'kpi':
            CTModuleManager.loadModule('kpi');
          break;
        default:
      }

      // fetch('http://localhost:8081/webapp/cat/index.ios.bundle?platform=ios&dev=true')
      // .then((response) => response.text())
      // .then((responseText) => {
      //   eval(responseText);
      // })
      // .catch((error) => {
      //   console.warn(error);
      // });
  },
  _renderChartGrid:function() {
		var grid = [],
			that = this,

			renderCell = function(row) {
				return row.map(function(chart) {
					return (
						<TouchableOpacity key={chart.id} style={styles.chartItem} onPress={that.onPress.bind(this,chart.id)}>
							<View style={styles.chartItemWrapper}>
								<View style={styles.chartItemIconWrapper}>
									<Image resizeMode="stretch" style={styles.chartItemIcon} source={chart.icon} />
								</View>
								<Text style={styles.chartItemText}>{chart.name}</Text>
							</View>
						</TouchableOpacity>
					);
				});
			},

			renderCellWidthAdd = function(row, i) {
				var items = renderCell(row);
        return items;
			};



  		chartList.forEach(function(chart, i) {
  			var rowIndex = Math.floor(i / 3);

  			if (i % 3 === 0) {
  				grid[rowIndex] = [];
  			}
  			grid[rowIndex].push(chart);
  		});

  		return grid.map(function(row, i) {
  			return (
  				<View style={styles.chartRow}>
  					{renderCellWidthAdd(row, i)}
  				</View>
  			);
  		});
	},
  render: function(){
    return (
			<View style={styles.container}>
        <View style={styles.chartList}>
					{this._renderChartGrid()}
				</View>
			</View>
		);


  }
});

var styles = StyleSheet.create({
	//container
  	container:{
    	backgroundColor:'white',
    	flex:1,
      top:100
  	},
  	chartList: {

  	},
  	chartRow: {
  		flexDirection: 'row'
  	},
  	chartItem: {
  		width: width / 3
  	},
  	chartItemWrapper: {
  		paddingTop: 0,
  		paddingBottom: 0,
  		paddingLeft: 0,
  		paddingRight: 0,
  		alignItems: 'center',
  		textAlign: 'center'
  	},
  	chartItemIconWrapper: {
  		width: 100,
  		height: 100,
  		marginBottom: 5,
  		justifyContent: 'center',
  		alignItems: 'center',
  		borderStyle: 'solid',
  		borderWidth: 1,
  		borderColor: '#c8c8c8',
  		borderRadius: 10,
  		backgroundColor: '#dfdfdf'
  	},
  	chartItemIcon: {
  		width: 80,
  		height: 80
  	},
  	chartItemText: {
  		fontSize: 24,
  		color: '#666'
  	},
  	addItemIconWrapper: {
  		borderWidth: 0,
  		backgroundColor: 'transparent'
  	},
  	addItemText: {
  		color: '#999'
  	}
});
AppRegistry.registerComponent('home', () => coldcat);