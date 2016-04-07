/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerCtrl', function($scope, $state, UserService, baseDataService, SUCCESS, FAILURE, CUSTOMER_ADD_URI, CUSTOMERGRADE_ALL_URI) {
    //set default data on the page
    initPageData();
    function initPageData() {
        if ( baseDataService.getIsPageNew()) {
            $scope.customer = {};
            $scope.customer.customerType='Individual';
        } else {
            $scope.customer = angular.copy(baseDataService.getRow());
            baseDataService.setIsPageNew(true);
            baseDataService.setRow({});
        }
        baseDataService.getBaseData(CUSTOMERGRADE_ALL_URI).then(function(response){
            $scope.gradeSet = response.data;
            $scope.customer.grade = baseDataService.populateSelectList($scope.customer.grade,$scope.gradeSet);
        });
    }

    //create new customer
    $scope.createCustomer = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.customer;
        baseDataService.addRow(rowObject, CUSTOMER_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listCustomer');
            } else {
                alert('Not able to save customer. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

});
