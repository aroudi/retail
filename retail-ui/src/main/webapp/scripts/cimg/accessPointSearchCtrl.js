/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('accessPointSearchCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, ACCESSPOINT_ALL_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'acptToken', visible:false,enableCellEdit:false, enableCellEdit:false},
            {field:'acptName', displayName:'Access Point',enableCellEdit:false, width:'45%',
                cellTooltip: function(row,col) {
                    return row.entity.acptName
                }
            },
            {field:'acptDesc', displayName:'Description',enableCellEdit:false, width:'45%',
                cellTooltip: function(row,col) {
                    return row.entity.acptDesc
                }
            }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
        });
    };
    getAllAccessPoint();
    function getAllAccessPoint() {
        baseDataService.getBaseData(ACCESSPOINT_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }
    $scope.submit = function () {
        if ($scope.selectedOption != undefined) {
            $scope.confirm($scope.gridApi.selection.getSelectedRows());
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

});
