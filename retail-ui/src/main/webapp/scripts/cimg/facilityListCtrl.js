/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('facilityListCtrl', function($scope, $state, baseDataService, SUCCESS, FAILURE, FACILITY_ALL_URI, FACILITY_STATUS_URI, FACILITY_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'status', cellFilter:'accessibilityStatus',
                cellClass:
                    function(grid, row, col, rowRenderIndex, colRenderIndex) {
                        if (grid.getCellValue(row, col) === true) {
                            return 'green';
                        } else {
                            return 'red'
                        }
                    },
                enableCellEdit:false, width:'5%'
            },
            {field:'station.name', displayName:'Station',enableCellEdit:false, width:'5%'},
            {field:'type', displayName:'Type',enableCellEdit:false, width:'5%'},
            {field:'name', displayName:'Name',enableCellEdit:false, width:'5%'},
            {field:'alternativeOption', displayName:'Alternative Customer Options',enableCellEdit:false, width:'20%'},
            {field:'customerCommunication', enableCellEdit:false , width:'20%'},
            {field:'twitterContent', enableCellEdit:false, width:'20%'},
            //{field:'linesAfected', displayName:'Lines Affected', enableCellEdit:false},
            {name:'linesAfected', displayName:'Lines Affected', cellFilter:'lineList', overflow:'auto', width:'10%'},
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editFacility(row)"></i></a>&nbsp;<a href=""><i tooltip="Delete" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ></i></a>&nbsp;<a href=""><i tooltip="Change Status" tooltip-placement="bottom" class="fa fa-wrench fa-2x" ng-click="grid.appScope.changeStatus(row)" ></i></a>', width:'10%' }
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
        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef, newValue, oldValue) {
            if (newValue === true) {
                colDef.cellClass = 'green';
            } else if (newValue === false) {
                colDef.cellClass='red';
            }
            $scope.$apply();
            $scope.gridApi.core.refresh();
        })
    };
    getAllFacilities();
    function getAllFacilities() {
        baseDataService.getBaseData(FACILITY_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editFacility = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var facilityGetURI = FACILITY_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(facilityGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the facility page.
            $state.go('dashboard.createFacilityMessage');
        });
    }

    $scope.changeStatus = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var facility = row.entity;
        if (facility.status == true) {
            facility.status = false;
            row.entity.status=false;
        } else {
            facility.status=true;
            row.entity.status=true;
        }
        baseDataService.addRow(facility, FACILITY_STATUS_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                getAllFacilities();
            } else {
                alert('Not able to save incident. ' + addResponse.message);
            }
        });
    }
});
