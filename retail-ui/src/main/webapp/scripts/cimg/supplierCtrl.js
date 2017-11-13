/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('supplierCtrl', function($scope, $state,$stateParams,viewMode, UserService,uiGridConstants, baseDataService, SUCCESS, FAILURE, SUPPLIER_ADD_URI, SUPPLIER_STATUS_URI, SUPPLIER_TYPE_URI, SUPPLIER_GET_PO_LIST_URI, SUPPLIER_GET_DN_LIST_URI, SUPPLIER_GET_PRODUCT_LIST_URI, POH_GET_URI,DEL_NOTE_GET_URI) {
    //set default data on the page
    $scope.isViewMode = false;
    if (viewMode!=undefined) {
        $scope.isViewMode = viewMode;
    }
    initProductList();
    initPurchaseOrderList();
    initDeliveryNoteList();
    initPageData();


    function initProductList() {
        $scope.productList = {
            enableFiltering: true,
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'solId', visible:false, enableCellEdit:false},
                {field:'prodId', visible:false, enableCellEdit:false},
                {field:'catalogueNo', displayName:'CatNo',enableCellEdit:false, width:'30%',
                    cellTooltip: function(row,col) {
                        return row.entity.catalogueNo
                    }
                },
                {field:'partNo', enableCellEdit:false, width:'20%',
                    cellTooltip: function(row,col) {
                        return row.entity.partNo
                    }
                },
                {field:'unitOfMeasure.unomCode', displayName:'Size',enableCellEdit:false,width:'10%'},
                {field:'unomQty',displayName:'Qty', enableCellEdit:true, type: 'number', width:'10%'},
                {field:'price', enableCellEdit:true, cellFilter: 'currency', width:'10%'},
                {field:'bulkQty', enableCellEdit:true, type: 'number', width:'10%'},
                {field:'bulkPrice', enableCellEdit:true, cellFilter: 'currency', width:'10%'}
            ]
        }
        $scope.productList.enableRowSelection = false;
        $scope.productList.multiSelect = false;
        $scope.productList.noUnselect= true;
    }

    function initPurchaseOrderList() {
        $scope.purchaseOrderList = {
            enableFiltering: true,
            enableSelectAll:true,
            enableRowSelection:true,
            showGridFooter: true,
            showColumnFooter: true,
            enableColumnResizing: true,
            enableSorting:true,
            expandableRowTemplate: '<div ui-grid="row.entity.subpurchaseOrderList" ui-grid-selection style ="height: 100px;"></div>',
            columnDefs: [
                {field:'id', visible:false, enableCellEdit:false},
                {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'25%'},
                {field:'pohCreatedDate', displayName:'Created',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'25%'},
                {field:'pohValueGross', displayName:'Gross Value',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field:'pohValueNett', displayName:'Net Value',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field:'pohStatus', displayName:'status',enableCellEdit:false, width:'15%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
            ]
        }
        $scope.purchaseOrderList.enableRowSelection = true;
        $scope.purchaseOrderList.multiSelect = false;

        //
        $scope.purchaseOrderList.onRegisterApi = function (gridApi) {
            $scope.purchaseOrderListGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
        };

    }
    function initDeliveryNoteList() {
        $scope.deliveryNoteList = {
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
                {field:'delnNoteNumber', displayName:'Note No',enableCellEdit:false, width:'15%'},
                {field:'delnGrn', displayName:'GRN',enableCellEdit:false, width:'15%'},
                {field:'pohOrderNumber', displayName:'Order No',enableCellEdit:false, width:'15%',
                    cellTemplate:'<a href="" ng-click="grid.appScope.viewPurchaseOrderDetail(row)">{{grid.appScope.getLinkedPurchaseOrder(row)}}</a>'
                },
                {field:'delnDeliveryDate', displayName:'Delivery Date',enableCellEdit:false, width:'10%', cellFilter:'date:\'dd/MM/yyyy HH:mm\''},
                {field:'user',  displayName:'Created By',enableFiltering:false, cellFilter:'fullName', enableCellEdit:false, width:'10%'},
                {field:'delnValueGross', displayName:'Gross Value',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field:'delnValueNett', displayName:'Net Value',enableCellEdit:false, width:'10%',cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
                {field:'delnStatus', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
            ]
        }
        $scope.deliveryNoteList.enableRowSelection = true;
        $scope.deliveryNoteList.multiSelect = true;
        //$scope.deliveryNoteList.noUnselect= true;

        //
        $scope.deliveryNoteList.onRegisterApi = function (gridApi) {
            $scope.deliveryNoteListGridApi = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function(row) {
                baseDataService.setRow(row.entity);
            });
        };
    }

    function initPageData() {
        if ( $stateParams.blankPage) {
            $scope.supplier = {};
            $scope.supplier.id = -1;
            $scope.supplier.contact = {};
            $scope.isPageNew = true;
        } else {
            $scope.isPageNew = false;
            $scope.supplier = angular.copy(baseDataService.getRow());
            //baseDataService.setIsPageNew(true);
            //baseDataService.setRow({});
            initSupplierRelatedData($scope.supplier);
        }
        baseDataService.getBaseData(SUPPLIER_STATUS_URI).then(function(response){
            $scope.supplierStatusSet = response.data;
            $scope.supplier.supplierStatus = baseDataService.populateSelectList($scope.supplier.supplierStatus,$scope.supplierStatusSet);
        });
        baseDataService.getBaseData(SUPPLIER_TYPE_URI).then(function(response){
            $scope.supplierTypeSet = response.data;
            $scope.supplier.supplierType = baseDataService.populateSelectList($scope.supplier.supplierType,$scope.supplierTypeSet);
        });
    }

    //create new supplier
    $scope.createSupplier = function () {

        /*
        var userId = UserService.getUserId();
        if (userId == undefined || userId == 0) {
            alert('you need to login first');
            $state.go('dashboard.login');
        }
        */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.supplier;
        baseDataService.addRow(rowObject, SUPPLIER_ADD_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.listSupplier');
            } else {
                alert('Not able to save supplier. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        //$state.go('dashboard.listFacility');
        $state.go($scope.previouseState);
    }

    function initSupplierRelatedData(supplier) {
        baseDataService.getBaseData(SUPPLIER_GET_PO_LIST_URI + supplier.id).then(function(response){
            $scope.purchaseOrderList.data = response.data;
        });

        baseDataService.getBaseData(SUPPLIER_GET_DN_LIST_URI + supplier.id).then(function(response){
            $scope.deliveryNoteList.data = response.data;
        });

        baseDataService.getBaseData(SUPPLIER_GET_PRODUCT_LIST_URI + supplier.id).then(function(response){
            $scope.productList.data = response.data;
        });
    }

    $scope.viewPohDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var pohGetURI = POH_GET_URI +  row.entity.id;
        baseDataService.getBaseData(pohGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.purchaseOrderDetail');
        });
    }

    $scope.viewDeliveryNoteDetail = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var delnGetURI = DEL_NOTE_GET_URI +  row.entity.id;
        baseDataService.getBaseData(delnGetURI).then(function(response){
            //baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.deliveryNote');
        });
    };

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    };

});
