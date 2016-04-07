/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('supplierCtrl', function($scope, $state, UserService, baseDataService, SUCCESS, FAILURE, SUPPLIER_ADD_URI, SUPPLIER_STATUS_URI, SUPPLIER_TYPE_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.supplier = {};
            $scope.supplier.id = -1;
            $scope.supplier.contact = {};
        } else {
            $scope.supplier = angular.copy(baseDataService.getRow());
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
        }
        baseDataService.getBaseData(SUPPLIER_STATUS_URI).then(function(response){
            $scope.supplierStatusSet = response.data;
            $scope.supplier.supplierStatus = baseDataService.populateSelectList($scope.supplier.supplierStatus,$scope.supplierStatusSet);
        });
        baseDataService.getBaseData(SUPPLIER_TYPE_URI).then(function(response){
            $scope.supplierTypeSet = response.data;
            $scope.supplier.supplierType = baseDataService.populateSelectList($scope.supplier.supplierType,$scope.supplierTypeSet);
        });
    }

    //create new supplier
    $scope.createSupplier = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.supplier;
        baseDataService.addRow(rowObject, SUPPLIER_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listSupplier');
            } else {
                alert('Not able to save supplier. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }
});
