/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('roleSearchCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, ROLE_ALL_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'roleCode', displayName:'Code',enableCellEdit:false, width:'30%'},
            {field:'roleName', displayName:'Role Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.roleName
                }
            },
            {field:'roleDesc', displayName:'Description',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.roleDesc
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
    getAllRoles();
    function getAllRoles() {
        baseDataService.getBaseData(ROLE_ALL_URI).then(function(response){
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
