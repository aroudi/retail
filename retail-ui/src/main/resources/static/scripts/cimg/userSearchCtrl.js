/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('userSearchCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, USER_ALL_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'usrName', displayName:'User Name', enableCellEdit:false, width:'20%'},
            {field:'usrFirstName', displayName:'First Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.usrFirstName
                }
            },
            {field:'usrSurName', enableCellEdit:false , width:'35%',
                cellTooltip: function(row,col) {
                    return row.entity.usrSurName
                }
            },
            {field:'usrActive', displayName:'Active',enableCellEdit:false, width:'5%', cellFilter:'booleanFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    if (grid.getCellValue(row, col) === 'Yes') {
                        return 'green';
                    } else if (grid.getCellValue(row, col) === 'No') {
                        return 'red'
                    }
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
    getAllUsers();
    function getAllUsers() {
        baseDataService.getBaseData(USER_ALL_URI).then(function(response){
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
