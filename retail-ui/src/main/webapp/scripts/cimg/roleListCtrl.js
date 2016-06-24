/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('roleListCtrl', function($scope, $state, $timeout,baseDataService, SUCCESS, FAILURE, ROLE_ALL_URI, ROLE_GET_URI) {
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
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editRole(row)"></i></a>', width:'5%' }
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
    getAllRoles();
    function getAllRoles() {
        baseDataService.getBaseData(ROLE_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editRole = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var roleGetURI = ROLE_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(roleGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the role page.
            $state.go('dashboard.addRole');
        });
    }
});
