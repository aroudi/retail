/**
 * Created by arash on 14/08/2015.
 */
function stationMessageCtrl( $scope, $state, baseDataService, UserService, SUCCESS, FAILURE, ALL_NET_LINE_URI, LINE_STATION_URI, STATION_MSG_ADD_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.stationMessage = {};
            $scope.pageIsNew = true;
        } else {
            $scope.stationMessage = angular.copy(baseDataService.getRow());
            //baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
            $scope.pageIsNew = false;
        }
        baseDataService.getBaseData(ALL_NET_LINE_URI).then(function(response){
            $scope.lineSet = response.data;
            $scope.stationMessage.lines = baseDataService.populateMultiSelectList($scope.stationMessage.lines,$scope.lineSet);
            $scope.changeLine();
        });

    }

    //create new facility
    $scope.createMessage = function () {

        if ($scope.pageIsNew) {
            $scope.stationMessage.id = -1;
        }
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        $scope.stationMessage.userId = userId;
        var rowObject = $scope.stationMessage;
        baseDataService.addRow(rowObject, STATION_MSG_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listIncident');
            } else {
                alert('Not able to save message. ' + addResponse.message);
            }
        });
        return;
    }

    $scope.changeLine = function (){
        var rowObject = $scope.stationMessage.lines;
        baseDataService.addRow(rowObject, LINE_STATION_URI).then(function(response) {
            $scope.stationSet = response.data;
            $scope.stationMessage.stations = baseDataService.populateMultiSelectList($scope.stationMessage.stations,$scope.stationSet);
            $scope.getAllStations();
        });
    };

    $scope.getAllLines = function (){
        if ($scope.allLines==true) {
            $scope.stationMessage.lines = $scope.lineSet;
            $scope.changeLine();
        }
    };
    $scope.getAllStations = function (){
        if ($scope.allStations==true) {
            $scope.stationMessage.stations = $scope.stationSet;

        }
    };
    $scope.cancelForm = function() {
        //$state.go('dashboard.listIncident');
        $state.go($scope.previouseState);
    }

}
