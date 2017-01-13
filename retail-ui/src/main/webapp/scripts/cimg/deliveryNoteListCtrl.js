/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('deliveryNoteListCtrl', function($scope, $state, uiGridConstants, ngDialog, purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE, DEL_NOTE_GET_ALL_URI, DEL_NOTE_GET_URI, POH_GET_URI, DEL_NOTE_SEARCH_URI) {
    $scope.searchForm = {};
    $scope.searchForm.supplierId = -1;
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohId', visible:false, enableCellEdit:false},
            {field:'delnNoteNumber', displayName:'Note No',enableCellEdit:false, width:'10%'},
            {field:'delnGrn', displayName:'GRN',enableCellEdit:false, width:'10%'},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'10%',
                cellTemplate:'<a href="" ng-click="grid.appScope.viewPurchaseOrderDetail(row)">{{grid.appScope.getLinkedPurchaseOrder(row)}}</a>'
            },
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'20%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'delnDeliveryDate', displayName:'Delivery Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'delnValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'delnValueNett', displayName:'Net Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'delnStatus', displayName:'status',enableCellEdit:false, width:'8%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                   return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="View Detail" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.viewDeliveryNoteDetail(row)"></i></a>', width:'5%' }
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            baseDataService.setRow(row.entity);
        });
    };

    getDeliveryNoteList();
    function getDeliveryNoteList() {
        baseDataService.getBaseData(DEL_NOTE_GET_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
        });
    }

    $scope.viewDeliveryNoteDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var delnGetURI = DEL_NOTE_GET_URI +  row.entity.id;
        baseDataService.getBaseData(delnGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.deliveryNote');
        });
    }

    $scope.viewPurchaseOrderDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            return;
        }
        var pohGetURI = POH_GET_URI +  row.entity.pohId;
        baseDataService.getBaseData(pohGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.purchaseOrderDetail');
        });
    }

    $scope.getLinkedPurchaseOrder = function (row) {
        if (row.entity.pohOrderNumber ==undefined || row.entity.pohOrderNumber ==null ) {
            return '***';
        }
        return row.entity.pohOrderNumber;
    };
    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.supplier = value;
                $scope.searchForm.supplierId = $scope.supplier.id;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.search = function() {
        baseDataService.addRow($scope.searchForm, DEL_NOTE_SEARCH_URI).then(function(response){
            $scope.gridOptions.data = response.data;
        });
    }

});
