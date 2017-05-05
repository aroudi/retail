/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderListCtrl', function($scope, $state, uiGridConstants, ngDialog, purchaseOrderService, $timeout,baseDataService, SUCCESS, FAILURE, POH_GET_ALL_URI, POH_GET_URI, DEL_NOTE_GET_URI, POH_SEARCH_URI, POH_EXPORT_PDF, POH_SEARCH_PAGING_URI, SUPPLIER_ALL_URI) {

    $scope.searchForm = {};
    $scope.searchForm.supplierId = -1;

    $scope.getPage = function(){
        $scope.searchForm.pageNo = paginationOptions.pageNumber*1 ;
        $scope.searchForm.pageSize = paginationOptions.pageSize;
        if ($scope.supplier != undefined) {
            $scope.searchForm.supplierId = $scope.supplier.id;
        } else {
            $scope.searchForm.supplierId = -1;
        }

        $scope.pohSrouceData = purchaseOrderService.getSourceOfData();
        if ($scope.pohSrouceData == 'Auto') {
            purchaseOrderService.setSrouceOfData('');
            $scope.gridOptions.data = purchaseOrderService.getGeneratedResultFromBoqList();
        } else {
            baseDataService.addRow($scope.searchForm, POH_SEARCH_PAGING_URI).then(function(response) {
                var result = angular.copy(response.data);
                $scope.gridOptions.totalItems = result.totalRecords;
                $scope.gridOptions.data = result.result;
                for (i=0; i<$scope.gridOptions.data.length; i++) {
                    displayLinkedDelNotes($scope.gridOptions.data[i])
                }
            });
        }
    }

    var paginationOptions = {
        pageNumber:1,
        pageSize:25,
        sort:null
    };

    $scope.gridOptions = {
        paginationPageSizes : [25,50,75,100],
        paginationPageSize:25,
        useExternalPagination: true,
        useExternalSorting:true,
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:false,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
        expandableRowScope:{
            viewDelNoteDetail : function(row) {
                if (row == undefined || row.entity == undefined) {
                    return;
                }
                var deliveryNoteGetURI = DEL_NOTE_GET_URI +  row.entity.delnId;
                baseDataService.getBaseData(deliveryNoteGetURI).then(function(response){
                    baseDataService.setIsPageNew(false);
                    baseDataService.setRow(response.data);
                    //redirect to the supplier page.
                    $state.go('dashboard.deliveryNote');
                });
            },

            getLinkedDelNote : function (row) {
                if (row.entity.delnNoteNumber ==undefined || row.entity.delnNoteNumber ==null ) {
                    return '***';
                }
                return row.entity.delnNoteNumber;
            },
            subGridVariable: 'subGridScopeVariable'
        } ,
        columnDefs: [
            {name:'Action', sortable:false,enableFiltering:false, cellTemplate:'<a href=""><i tooltip="Open" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.viewPohDetail(row, false)"></i></a>&nbsp;<a href=""><i tooltip="View" tooltip-placement="bottom" class="fa fa-eye fa-2x" ng-click="grid.appScope.viewPohDetail(row, true)"></i></a>&nbsp;<a href=""><i tooltip="Print" tooltip-placement="bottom" class="fa fa-print fa-2x" ng-click="grid.appScope.exportToPdf(row)"></i></a>', width:'8%' },
            {field:'id', visible:false, enableCellEdit:false},
            {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'8%',
                cellTemplate:'<a href="" ng-click="grid.appScope.viewPohDetail(row)">{{row.entity.pohOrderNumber}}</a>'
            },
            {field:'pohRevision', displayName:'Rev.',enableCellEdit:false, width:'5%'},
            {field:'supplier.supplierName', displayName:'Supplier',enableCellEdit:false, width:'29%',
                cellTooltip: function(row,col) {
                    return row.entity.supplier.supplierName
                }
            },
            {field:'pohCreatedDate', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
            {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
            {field:'pohValueGross', displayName:'Gross Value',enableCellEdit:false, width:'7%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohValueNett', displayName:'Net Value',enableCellEdit:false, width:'8%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'pohStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                   return grid.getCellValue(row, col).color
                }
            }
        ]
    }
    $scope.gridOptions.enableRowSelection = false;
    $scope.gridOptions.multiSelect = false;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
            if (sortColumns.length == 0) {
                paginationOptions.sort = null;
            } else {
                paginationOptions.sort = sortColumns[0].sort.direction;
            }
            $scope.getPage();
        });
        gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize) {
            console.log('newPage =' + newPage + ' pageSize=' + pageSize);
            paginationOptions.pageNumber = newPage;
            paginationOptions.pageSize = pageSize;
            $scope.getPage();
        });
    };

    initPageData();
    function initPageData() {
        $scope.searchForm = {};
        $scope.getPage();
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            if ($scope.supplierSet.length > 0) {
                var supplier = {
                    "id" : -1,
                    "supplierName" : "All"
                }
                $scope.supplierSet.unshift(supplier);
            }
        });
    }

    $scope.viewPohDetail = function(row, viewMode) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var pohGetURI = POH_GET_URI +  row.entity.id;
        baseDataService.getBaseData(pohGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            if (viewMode) {
                ngDialog.openConfirm({
                    template:'views/pages/purchaseOrderDetail.html',
                    controller:'purchaseOrderDetailCtrl',
                    className: 'ngdialog-pdfView',
                    closeByDocument:false,
                    resolve: {viewMode: function(){return true},
                        taxCodeSet: function(baseDataService, TAXLEGVARIANCE_ALL_URI){
                            return baseDataService.getBaseData(TAXLEGVARIANCE_ALL_URI);
                        }
                    }
                }).then (function (){
                    }, function(reason) {
                        console.log('Modal promise rejected. Reason:', reason);
                    }
                );
            } else {
                $state.go('dashboard.purchaseOrderDetail');
            }
        });
    }


    function displayLinkedDelNotes(line) {
        line.subGridOptions = {
            enableRowSelection :false,
            enableColumnResizing: true,
            columnDefs :[
                {field:"id", visible:false},
                {field:"pohId", visible:false},
                {field:"delnId", visible:false},
                {field:"delnNoteNumber", displayName:"Delivery Note No.", visible:true, width:'40%',
                    cellTemplate:'<a href="" ng-click="grid.appScope.viewDelNoteDetail(row)">{{grid.appScope.getLinkedDelNote(row)}}</a>'
                },
                {field:"delnGrn", displayName:"GRN No", visible:true, width:'40%'},
                {field:"delnDeliveryDate", displayName:"Delivery Date", width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\'' }
            ],
            data:line.linkedDelNotes
        }
    }

    /*
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
    */
    $scope.exportToPdf = function(row) {
        var exportUrl = POH_EXPORT_PDF + row.entity.id;
        baseDataService.pdfViewer(exportUrl);
    }
});
