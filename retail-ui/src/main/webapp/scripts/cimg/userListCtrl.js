/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('userListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, USER_ALL_URI, USER_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Action',enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editUser(row)"></i></a>', width:'5%' },
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
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };
    getAllUsers();
    function getAllUsers() {
        baseDataService.getBaseData(USER_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editUser = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var userGetURI = USER_GET_URI  + row.entity.id;
        baseDataService.getBaseData(userGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the user page.
            $state.go('dashboard.addUser');
        });
    }
});
