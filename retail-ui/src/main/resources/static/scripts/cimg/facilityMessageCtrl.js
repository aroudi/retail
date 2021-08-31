/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('facilityMessageCtrl', function($scope, $state, UserService, baseDataService, SUCCESS, FAILURE, FACILITY_ADD_URI, ALL_LINE_URI, STATION_NAME_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.facility = {};
        } else {
            $scope.facility = angular.copy(baseDataService.getRow());
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
        }
        baseDataService.getBaseData(ALL_LINE_URI).then(function(response){
            $scope.lineSet = response.data;
            $scope.facility.linesAfected = baseDataService.populateMultiSelectList($scope.facility.linesAfected,$scope.lineSet);
        });
        baseDataService.getBaseData(STATION_NAME_URI).then(function(response){
            $scope.stationSet = response.data;
            $scope.facility.station = baseDataService.populateSelectList($scope.facility.station, $scope.stationSet);
        });

    }

    //create new facility
    $scope.createFacility = function () {

        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        $scope.facility.userId = userId;
        var rowObject = $scope.facility;
        baseDataService.addRow(rowObject, FACILITY_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listFacility');
            } else {
                alert('Not able to save incident. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

})
