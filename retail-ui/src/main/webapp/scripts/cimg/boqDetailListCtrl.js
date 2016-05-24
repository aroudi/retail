/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailListCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE,  PRODUCT_GET_URI, UPDATE_BOQ_STOCK_URI, POH_GET_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        //expandableRowTemplate: 'views/pages/treeViewTemplate.html',
        /*
        expandableRowScope:{
            subGridVariable: 'subGridScopeVariable'
        } ,
        */
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'supplier.supplierName', displayName:'Supplier', enableCellEdit:false, width:'15%'},
            {field:'product.prodSku', displayName:'SKU',enableCellEdit:false, width:'15%',
                cellTooltip: function(row,col) {
                    return row.entity.product.prodName
                }
            },
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'5%'},
            {field:'cost', displayName:'Cost',enableCellEdit:false, width:'5%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'quantity', displayName:'Qty',enableCellEdit:false, width:'5%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'qtyOnStock', displayName:'Stock', width:'5%', type: 'number', enableCellEdit:true, cellClass:"blue"},
            {field:'comment', displayName:'Location', width:'10%', enableCellEdit:true, cellClass:"blue"},
            {field:'qtyPurchased', displayName:'Purchased', enableCellEdit:false, width:'7%', aggregationType: uiGridConstants.aggregationTypes.sum, type: 'number'},
            {field:'qtyBalance', displayName:'Balance', enableCellEdit:false, width:'8%', aggregationType: uiGridConstants.aggregationTypes.sum, type: 'number'},
            /*
            {field:'prodIsNew', displayName:'New Item',enableCellEdit:false, width:'8%', cellFilter:'booleanFilter',
                cellClass:
                    function(grid, row, col, rowRenderIndex, colRenderIndex) {
                        if (grid.getCellValue(row, col) === true) {
                            return 'red';
                        } else {
                            return 'green'
                        }
                    }
            },
            */
            {field:'linkedPurchaseOrders', displayName:'Purchase Order No',enableCellEdit:false, width:'10%', cellFilter:'poBoqLinkOrderNumberFilter',
                cellTemplate:'<a href="" ng-click="grid.appScope.viewPohDetail(row)">{{grid.appScope.getPoBoqLinkOrderNo(row)}}</a>'
            },
            {field:'bqdStatus', displayName:'status',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="Edit Product" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-click="grid.appScope.editProduct(row)"></i></a>', width:'5%' }

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
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'qtyOnStock') {
                $scope.qtyOnStockBeforeEditting = rowEntity.qtyOnStock;
                //alert('$scope.qtyOnStockBeforeEditting = ' + $scope.qtyOnStockBeforeEditting);
            }
        })
    };
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var boqDetail = event.targetScope.row.entity;
        cellData = event.targetScope.row.entity[event.targetScope.col.field];
        //txnDetail.txdeQuantitySold = cellData;
        if (boqDetail.bqdStatus.displayName!='PENDING') {
            baseDataService.displayMessage('Warning!!','You can only change the value on PENDING status');
            boqDetail.qtyOnStock = $scope.qtyOnStockBeforeEditting;
            return;
        }
        if (boqDetail.qtyOnStock > boqDetail.quantity) {
            baseDataService.displayMessage('Warning!!','Invalid number');
            boqDetail.qtyOnStock = $scope.qtyOnStockBeforeEditting;
            //event.targetScope.col.field.value = $scope.qtyOnStockBeforeEditting;
            return;
        }
        boqDetail.qtyBalance = boqDetail.quantity - (boqDetail.qtyOnStock + boqDetail.qtyPurchased);
    })

    $scope.$on('uiGridEventStartCellEdit', function (event) {
        var boqDetail = event.targetScope.row.entity;
        $scope.qtyOnStockBeforeEditting = event.targetScope.row.entity[event.targetScope.col.field];
        alert('before editting value = ' + $scope.qtyOnStockBeforeEditting);
    })

    initPageData();
    function initPageData() {
        $scope.billOfQuantity = angular.copy(baseDataService.getRow());
        /*
        for (i=0; i<$scope.billOfQuantity.lines.length; i++) {
            displayLinkedPurchaseOrders($scope.billOfQuantity.lines[i])
        }
        */
        $scope.gridOptions.data = $scope.billOfQuantity.lines;
        baseDataService.setRow({});
    }

    $scope.editProduct = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('row is undefined');
            return;
        }
        var productGetURI = PRODUCT_GET_URI + '/' + row.entity.product.id;
        baseDataService.getBaseData(productGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.createProduct');
        });
    }
    $scope.updateBoqStock = function () {

        /*
         var userId = UserService.getUserId();
         if (userId == undefined || userId == 0) {
         alert('you need to login first');
         $state.go('dashboard.login');
         }
         */

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.billOfQuantity;
        baseDataService.addRow(rowObject, UPDATE_BOQ_STOCK_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.boqList');
            } else {
                alert('Not able to update stock quantity. ' + addResponse.message);
            }
        });
        return;
    }
    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    function displayLinkedPurchaseOrders(boqLine) {
        boqLine.subGridOptions = {
                enableRowSelection :false,
                enableColumnResizing: true,
                columnDefs :[
                    {name:"id", field:"id", visible:false},
                    {name:"pohId", field:"pohId", visible:false},
                    {name:"polId", field:"polId", visible:false},
                    {name:"boqId", field:"boqId", visible:false},
                    {name:"boqDetailId", field:"boqDetailId", visible:false},
                    {name:"pohOrderNumber", field:"pohOrderNumber", displayName:"Purchase No", width:'20%',
                        cellTooltip: function(row,col) {
                            return row.entity.pohOrderNumber
                        }
                    },
                    {name: "boqQtyTotal", field: "boqQtyTotal", displayName:"Qty Ordered", width: '10%'},
                    {name: "poQtyReceived", field: "poQtyReceived", displayName:"Qty Received", width: '10%'},
                    {name: "boqQtyBalance", field: "boqQtyBalance", displayName:"Qty Balance", width: '10%'},
                    {field:'status', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                        cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                            return grid.getCellValue(row, col).color
                        }
                    }
                ],
                data:boqLine.linkedPurchaseOrders
            }
        }

    $scope.viewPohDetail = function(row) {
        if (row == undefined || row.entity == undefined || row.entity.linkedPurchaseOrders == undefined || row.entity.linkedPurchaseOrders == null || row.entity.linkedPurchaseOrders.length < 1) {
            return;
        }
        var pohGetURI = POH_GET_URI +  row.entity.linkedPurchaseOrders[0].pohId;
        baseDataService.getBaseData(pohGetURI).then(function(response){
            baseDataService.setIsPageNew(false);
            baseDataService.setRow(response.data);
            //redirect to the supplier page.
            $state.go('dashboard.purchaseOrderDetail');
        });
    }

    $scope.getPoBoqLinkOrderNo = function (row) {
        if (row.entity.linkedPurchaseOrders ==undefined || row.entity.linkedPurchaseOrders ==null || row.entity.linkedPurchaseOrders.length < 1 ) {
            return '***';
        }
        return row.entity.linkedPurchaseOrders[0].pohOrderNumber;
    };
    $scope.formatDate = function(value) {
        return $filter('date')(value, 'medium');
    }
});
