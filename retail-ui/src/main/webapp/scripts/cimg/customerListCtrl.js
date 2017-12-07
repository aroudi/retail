/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('customerListCtrl', function($scope, $state, $timeout, ngDialog, baseDataService, SUCCESS, FAILURE, CUSTOMER_ALL_URI, CUSTOMER_GET_URI) {
    $scope.gridOptions = {
        enableFiltering: true,
        columnDefs: [
            {name:'Action', enableFiltering:false,cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editCustomer(row, false)"></i></a><a href="">&nbsp;&nbsp;<i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.editCustomer(row, true)"></i></a>', width:'5%' },
            {field:'customerId', visible:false, enableCellEdit:false},
            {field:'customerType', displayName:'Terms',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'surName', displayName:'Surname',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.surName
                }
            },
            {field:'firstName', displayName:'Given Names',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.firstName
                }
            },
            {field:'code', displayName:' ', enableCellEdit:false, width:'10%'},
            {field:'companyName', displayName:'Company Name',enableCellEdit:false, width:'25%',
                cellTooltip: function(row,col) {
                    return row.entity.companyName
                }
            },
            {field:'customerStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    $scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        /*
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
        */
    };
    getAllCustomers();
    function getAllCustomers() {
        baseDataService.getBaseData(CUSTOMER_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.editCustomer = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var customerGetURI = CUSTOMER_GET_URI + '/' + row.entity.id;
        baseDataService.getBaseData(customerGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the customer page.
            if (viewMode) {
                ngDialog.openConfirm({
                    template:'views/pages/customer.html',
                    controller:'customerCtrl',
                    className: 'ngdialog-pdfView',
                    closeByDocument:false,
                    resolve: {viewMode: function(){return true}
                    }
                }).then (function (){
                    }, function(reason) {
                        console.log('Modal promise rejected. Reason:', reason);
                    }
                );
            } else {
                $state.go('dashboard.createCustomer');
            }
        });
    }
});
