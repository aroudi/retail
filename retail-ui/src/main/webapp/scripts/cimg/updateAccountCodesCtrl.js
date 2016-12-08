/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('updateAccountCodesCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, ACCOUNT_GET_ALL_URI, ACCOUNT_UPDATE_CODE_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'accType.description', displayName:'Type', enableCellEdit:false, width:'25%'},
            {field:'accDesc', displayName:'Acc Name',enableCellEdit:false, width:'25%'},
            {field:'accCode', displayName:'Acc Code',width:'25%'}
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
        });
    };
    //set default data on the page
    initPageData();
    function initPageData() {
        baseDataService.getBaseData(ACCOUNT_GET_ALL_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }

    $scope.update = function () {
        baseDataService.addRow($scope.gridOptions.data, ACCOUNT_UPDATE_CODE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.updateAccountCodes');
            } else {
                baseDataService.displayMessage('info','Warning', 'Not able to save account. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }
});
