/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailSearchCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout ,baseDataService, POL_CREATION_TYPE_MANUAL, POH_GET_ALL_POH_NOT_FULLY_RECEIVED_URI, POL_CREATION_TYPE_MANUAL, BOQ_LINE_STATUS_PO_CREATED) {

    initPageData();
    function initPageData() {
        $scope.purchaseOrderHeader = {};
        $scope.boqDetail = {};
        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            $scope.boqDetail.bqdCreationType = response.data;
        });
        baseDataService.getBaseData(BOQ_LINE_STATUS_PO_CREATED).then(function(response){
            $scope.boqDetail.bqdStatus = response.data;
        });
    }
    var rowtpl='<div ng-class="{\'brown\':row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\'}"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" ui-grid-cell></div></div>';
    $scope.gridOptions = {
        enableFiltering: true,
        showGridFooter: true,
        showColumnFooter: true,
        enableColumnResizing: true,
        enableSorting:true,
        expandableRowTemplate: '<div ui-grid="row.entity.subGridOptions" ui-grid-selection style ="height: 100px;"></div>',
        expandableRowScope:{
            subGridVariable: 'subGridScopeVariable'
        } ,
        rowTemplate : rowtpl,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'20%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'8%'},
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:false, width:'7%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polQtyOrdered', displayName:'Ordered',enableCellEdit:false, width:'8%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            //{field:'polValueOrdered', displayName:'Value',enableCellEdit:false, width:'7%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'calculateQtyAssigned()', displayName:'Available', enableCellEdit:false,width:'7%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'boqQuantity', displayName:'BOQ Qty',enableCellEdit:true, width:'10%',type: 'number',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'qtyOnStock', displayName:'In Stock Qty',enableCellEdit:true, width:'10%',type: 'number',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'comment', displayName:'In Stock Location', enableCellEdit:true, width:'10%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'qtyBalance', displayName:'Balance',enableCellEdit:false, width:'7%',type: 'number',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'changeComment', displayName:'Comments', enableCellEdit:true, width:'13%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            }
            /*{field:'polCreationType', displayName:'Type',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'polStatus', displayName:'Status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            }*/
        ]
    }
    $scope.gridOptions.enableRowSelection = true;
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'boqQuantity') {
                $scope.boqQuantityBeforeEditting = rowEntity.boqQuantity;
            }
            if (colDef.name == 'qtyBalance') {
                $scope.qtyBalanceBeforeEditting = rowEntity.qtyBalance;
            }
        });

        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef){
        })
    };

    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var rowEntity = event.targetScope.row.entity;
        if (rowEntity.qtyOnStock == undefined) {
            rowEntity['qtyOnStock'] = 0;
        }
        if (rowEntity.boqQuantity == undefined) {
            rowEntity['boqQuantity'] = calculateQtyAssigned(rowEntity);
        }
        if (rowEntity.boqQuantity > calculateQtyAssigned(rowEntity)) {
            baseDataService.displayMessage('Warning','Invalid Qty', 'Qty entered is more than available qty');
            rowEntity['boqQuantity'] = $scope.boqQuantityBeforeEditting;
            return
        }
        rowEntity['qtyBalance'] =rowEntity.boqQuantity*1 - rowEntity.qtyOnStock*1;
    });

    function displayLinkedBoqs(line) {
        line.subGridOptions = {
            enableRowSelection :false,
            enableColumnResizing: true,
            enableRowEdit : true,
            columnDefs :[
                {name:"id", field:"id", visible:false},
                {name:"pohId", field:"pohId", visible:false},
                {name:"polId", field:"polId", visible:false},
                {name:"boqId", field:"boqId", visible:false},
                {name:"projectId", field:"projectId", visible:false},
                {name:"boqDetailId", field:"boqDetailId", visible:false},
                {name:"boqName", field:"boqName", displayName:"BOQ Name", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.boqName
                    }
                },
                {name:"projectCode", field:"projectCode", displayName:"Project Code", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.projectCode
                    }
                },
                {name: "boqQtyTotal", field: "boqQtyTotal", displayName:"BOQ Total", width: '15%'},
                {name: "poQtyReceived", enableCellEdit:true, field: "poQtyReceived", displayName:"Qty Received", width: '10%',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return 'editModeColor'
                    }
                },
                {name: "boqQtyBalance", field: "boqQtyBalance", displayName:"BOQ Balance", width: '15%'},
                {field:'status', displayName:'status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                    cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                        return grid.getCellValue(row, col).color
                    }
                }
                //{name:'Action', width:'5%',cellTemplate:'<button type="button" ng-show="row.grid.appScope.row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\'" class="btn btn-success" ng-click="grid.appScope.updateBoqQtyRcvd(row)" >Update</button>'}
            ],
            data:line.poBoqLinks
        }
    }
    $scope.searchPurchaseOrder = function () {
        ngDialog.openConfirm({
            template:'views/pages/purchaseOrderSearch.html',
            controller:'purchaseOrderSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {searchUrl: function(){return POH_GET_ALL_POH_NOT_FULLY_RECEIVED_URI}}
        }).then (function (selectedItem){
                if (selectedItem != undefined) {
                    $scope.purchaseOrderHeader = selectedItem;
                    $scope.gridOptions.data = $scope.purchaseOrderHeader.lines;
                    angular.forEach($scope.gridOptions.data,function(row){
                        row.calculateQtyAssigned = function() {
                            var qty = this.polQtyOrdered;
                            if (this.poBoqLinks == undefined || this.poBoqLinks.length < 1) {
                                return qty;
                            }
                            for (i=0; i < this.poBoqLinks.length; i++) {
                                qty = qty - this.poBoqLinks[i].boqQtyTotal;
                            }
                            //this.boqQuantity = qty;
                            return qty;
                        }
                    });
                    $scope.purchaseOrderHeader.pohExpDeliveryStr = new Date($scope.purchaseOrderHeader.pohExpDeliveryStr);
                    for (i=0; i<$scope.gridOptions.data.length; i++) {
                        $scope.gridOptions.data[i].boqQuantity = $scope.gridOptions.data[i].calculateQtyAssigned();
                        displayLinkedBoqs($scope.gridOptions.data[i]);
                    }
                    baseDataService.setRow({});
                    baseDataService.setIsPageNew(true);
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.submit = function () {
        var purchaseLineList = $scope.gridApi.selection.getSelectedRows();
        if (purchaseLineList.length < 1) {
            baseDataService.displayMessage('info','No item', 'No item is selected.');
            return;
        }
        var boqList = [];
        for (var i=0; i < purchaseLineList.length; i++) {
                var purchaseLine = purchaseLineList[i];
                var product = {
                    id : purchaseLine.purchaseItem.prodId,
                    prodSku : purchaseLine.purchaseItem.catalogueNo
                };
                var boqDetailObject = {
                    'id':-1,
                    'supplier':$scope.purchaseOrderHeader.supplier,
                    'product':product,
                    'unitOfMeasure' : purchaseLine.unitOfMeasure,
                    'cost' : purchaseLine.polUnitCost,
                    'quantity' : purchaseLine.boqQuantity,
                    'itemValue' : purchaseLine.boqQuantity*purchaseLine.polUnitCost ,
                    'qtyOnStock' : purchaseLine.qtyOnStock,
                    'comment' : purchaseLine.comment,
                    'changeComment' : purchaseLine.changeComment,
                    'qtyBalance' :  purchaseLine.qtyBalance,
                    'bqdCreationType' :  $scope.boqDetail.bqdCreationType,
                    'bqdStatus' :  $scope.boqDetail.bqdStatus,
                    'qtyPurchased' : purchaseLine.polQtyOrdered,
                    'qtyReceived' : 0.00
                }
                //create PO_BOQ_Link object
                var poBoqLink = {
                    id: -1,
                    pohId : $scope.purchaseOrderHeader.id,
                    pohOrderNumber : $scope.purchaseOrderHeader.pohOrderNumber,
                    polId : purchaseLine.id,
                    boqId : -1,
                    boqName :'',
                    projectId:'',
                    projectCode:'',
                    boqDetailId:'',
                    boqQtyTotal:purchaseLine.boqQuantity,
                    poQtyReceived:0,
                    boqQtyBalance:purchaseLine.boqQuantity,
                    status: $scope.boqDetail.bqdStatus
                };

                var poBoqLinks = [];
                poBoqLinks.push(poBoqLink);
                boqDetailObject.linkedPurchaseOrders = poBoqLinks;
                console.log('boqDetailObject.product = ' + boqDetailObject.product.id);
                boqList.push(boqDetailObject);
        }
        $scope.confirm(boqList);
    };

    function calculateQtyAssigned(purchaseLine) {
        var qty = purchaseLine.polQtyOrdered;
        if (purchaseLine.poBoqLinks == undefined || purchaseLine.poBoqLinks.length < 1) {
            return qty;
        }
        for (i=0; i < purchaseLine.poBoqLinks.length; i++) {
            qty = qty - purchaseLine.poBoqLinks[i].boqQtyTotal;
        }
        return qty;
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }
    $scope.formatDate = function(value) {
        return $filter('date')(value, 'medium');
    }

});
