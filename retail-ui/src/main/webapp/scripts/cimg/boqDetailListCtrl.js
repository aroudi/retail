/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('boqDetailListCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE,  PRODUCT_GET_URI, UPDATE_BOQ_STOCK_URI, POH_GET_URI, BOQ_EXPORT_PICKING_SLIP_PDF) {

    var rowtpl='<div ng-class="{\'red\':row.entity.bqdStatus.categoryCode==\'BOQ_LINE_STATUS_VOID\'}"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        gridFooterTemplate:"<div id=\"currency-default\"> Total:{{grid.appScope.billOfQuantity.boqValueGross | currency}}</div>",
        enableColumnResizing: true,
        enableSorting:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
        expandableRowScope:{
            subGridVariable: 'subGridScopeVariable'
        } ,
        rowTemplate : rowtpl,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'supplier.supplierName', displayName:'Supplier', enableCellEdit:false, width:'10%'},
            {field:'product.prodSku', displayName:'SKU',enableCellEdit:false, width:'15%',
                cellTooltip: function(row,col) {
                    return row.entity.product.prodName
                }
            },
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'5%'},
            {field:'cost', displayName:'Cost',enableCellEdit:false, width:'5%', cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'quantity', displayName:'Qty',enableCellEdit:false, width:'5%',type: 'number'},
            {field:'itemValue', displayName:'Total', enableCellEdit:false, width:'5%', cellFilter: 'currency', footerCellFilter: 'currency'},
            {field:'qtyOnStock', displayName:'Stock', width:'5%', type: 'number', enableCellEdit:true, cellClass:"blue"},
            {field:'comment', displayName:'Location', width:'5%', enableCellEdit:true, cellClass:"blue"},
            {field:'qtyBalance', displayName:'Balance', enableCellEdit:false, width:'5%', type: 'number'},
            {field:'linkedPurchaseOrders', displayName:'Purchase Order',enableCellEdit:false, width:'9%', cellFilter:'poBoqLinkOrderNumberFilter',
                cellTemplate:'<a href="" ng-click="grid.appScope.viewPohDetail(row)">{{grid.appScope.getPoBoqLinkOrderNo(row)}}</a>'
            },
            {field:'changeComment', displayName:'Comment', width:'8%', enableCellEdit:true, cellClass:"blue"},
            {field:'bqdCreationType', displayName:'Created',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'bqdStatus', displayName:'status',enableCellEdit:false, width:'7%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', cellTemplate:'<a href=""><i tooltip="void item" tooltip-placement="bottom" class="fa fa-close fa-2x" ng-show="grid.appScope.showVoidButton(row.entity)" ng-click="grid.appScope.voidItem(row)"></i></a><a href=""><i tooltip="delete item" tooltip-placement="bottom" class="fa fa-trash-o fa-2x" ng-click="grid.appScope.removeItem(row)" ng-show="row.entity.id < 0 "></i></a>', width:'10%' }

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
        for (i=0; i<$scope.billOfQuantity.lines.length; i++) {
            displayLinkedPurchaseOrders($scope.billOfQuantity.lines[i])
        }
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
        for (i=0; i<$scope.billOfQuantity.lines.length; i++) {
            if ($scope.billOfQuantity.lines[i].subGridOptions != undefined ||$scope.billOfQuantity.lines[i].subGridOptions != null) {
                delete $scope.billOfQuantity.lines[i].subGridOptions;
            }
        }

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

    $scope.voidItem = function (row) {
        var boqBackUpItem = angular.copy(row.entity);
        var boqOriginalItem = row.entity;

        ngDialog.openConfirm({
            template:'views/pages/addComment.html',
            controller:'boqDetailVoidLineCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {boqItem: function(){return boqOriginalItem}}
        }).then (function (updatedLine){
                if (updatedLine != undefined) {
                    row.entity = updatedLine;
                    $scope.billOfQuantity.boqValueGross = $scope.billOfQuantity.boqValueGross - row.entity.itemValue;
                }
            }, function(reason) {
                row.entity = boqBackUpItem;
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }

    $scope.addItem = function () {
        ngDialog.openConfirm({
            template:'views/pages/boqDetailAddLine.html',
            controller:'boqDetailAddLineCtrl',
            className: 'ngdialog-theme-plain',
            closeByDocument:false
            //resolve: {supplier: function(){return $scope.purchaseOrderHeader.supplier}}
        }).then (function (selectedItem){
                if (selectedItem != undefined) {
                    $scope.gridOptions.data.push(selectedItem);
                    $scope.billOfQuantity.boqValueGross = $scope.billOfQuantity.boqValueGross + selectedItem.itemValue;
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.addItemFromPurchaseOrder = function () {
        ngDialog.openConfirm({
            template:'views/pages/purchaseOrderDetailSearch.html',
            controller:'purchaseOrderDetailSearchCtrl',
            className: 'ngdialog-theme-plain',
            closeByDocument:false
            //resolve: {supplier: function(){return $scope.purchaseOrderHeader.supplier}}
        }).then (function (selectedItem){
                if (selectedItem != undefined) {
                    if (selectedItem.linkedPurchaseOrders != undefined && selectedItem.linkedPurchaseOrders.length > 0) {
                        for (i =0; i<selectedItem.linkedPurchaseOrders.length; i++) {
                            selectedItem.linkedPurchaseOrders[i].boqId = $scope.billOfQuantity.id;
                            selectedItem.linkedPurchaseOrders[i].boqName = $scope.billOfQuantity.boqName;
                            selectedItem.linkedPurchaseOrders[i].projectId = $scope.billOfQuantity.project.id;
                            selectedItem.linkedPurchaseOrders[i].projectId = $scope.billOfQuantity.project.id;
                            selectedItem.linkedPurchaseOrders[i].projectCode = $scope.billOfQuantity.project.projectCode;
                        }
                    }
                    displayLinkedPurchaseOrders(selectedItem);
                    $scope.gridOptions.data.push(selectedItem);
                    $scope.billOfQuantity.boqValueGross = $scope.billOfQuantity.boqValueGross + selectedItem.itemValue;
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.exportToPdf = function() {
        var hiddenElement = document.createElement('a');
        var exportUrl = BOQ_EXPORT_PICKING_SLIP_PDF + $scope.billOfQuantity.id;
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            //saveAs (blob , outPutFile);
            hiddenElement.href = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            //hiddenElement.target ='_blank';
            hiddenElement.download = 'pickingSlip.pdf';
            hiddenElement.click();
        });

    }
    $scope.removeItem = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        if (!confirm('Are you sure you want to delete this item?')) {
            return;
        }
        row.entity.deleted = true;
        $scope.gridApi.core.setRowInvisible(row);
    }
    $scope.showVoidButton = function(item) {
        //BOQ_LINE_STATUS_PO_CREATED
        if (item.id < 0) {
            return false;
        }
        if ( (item.bqdCreationType.categoryCode == 'POH_CREATION_TYPE_MANUAL')&&(item.bqdStatus.categoryCode == 'BOQ_LINE_STATUS_PENDING' || item.bqdStatus.categoryCode == 'BOQ_LINE_STATUS_PO_CREATED')) {
            return true;
        }
        if ( (item.bqdCreationType.categoryCode == 'POH_CREATION_TYPE_AUTO')&&(item.bqdStatus.categoryCode == 'BOQ_LINE_STATUS_PENDING') ) {
            return true;
        }
        return false;
    }
});
