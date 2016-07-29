/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailSearchCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout ,baseDataService, POL_CREATION_TYPE_MANUAL, POH_GET_ALL_POH_NOT_FULLY_RECEIVED_URI, POL_CREATION_TYPE_MANUAL, BOQ_LINE_STATUS_PO_CREATED) {

    initPageData();
    function initPageData() {
        $scope.enableSubmit = false;
        $scope.purchaseOrderHeader = {};
        $scope.boqDetail = {};
        $scope.boqDetail.quantity = 0.0;
        $scope.boqDetail.qtyOnStock = 0.0;
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
            {field:'purchaseItem.catalogueNo', displayName:'Catalogue No', enableCellEdit:false, width:'25%'},
            {field:'unitOfMeasure.unomDesc', displayName:'Size', enableCellEdit:false, width:'10%'},
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:false, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polQtyOrdered', displayName:'Qty Ordered',enableCellEdit:false, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polValueOrdered', displayName:'Value',enableCellEdit:false, width:'7%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'calculateQtyAssigned()', displayName:'Balance', enableCellEdit:false,width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polCreationType', displayName:'Type',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'polStatus', displayName:'Status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
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
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
            var quantityCanBeAssignedFromPO = calculateQtyAssigned($scope.selectedOption);
            if (quantityCanBeAssignedFromPO > 0) {
                $scope.boqDetail.quantity = quantityCanBeAssignedFromPO;
                $scope.boqDetail.qtyBalance =quantityCanBeAssignedFromPO;
                $scope.enableSubmit = true;
            } else {
                baseDataService.displayMessage('info','Invalid Quantity', 'Quantity is invalid')
                $scope.enableSubmit = false;
            }

        });
        gridApi.edit.on.beginCellEdit($scope, function(rowEntity, colDef){
        })

        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef){
        })
    };


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
                    var purchaseOrderHeader = selectedItem;
                    $scope.purchaseOrderHeader = angular.copy(purchaseOrderHeader);
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
                            return qty;
                        }
                    });
                    $scope.purchaseOrderHeader.pohExpDeliveryStr = new Date($scope.purchaseOrderHeader.pohExpDeliveryStr);
                    for (i=0; i<$scope.gridOptions.data.length; i++) {
                        displayLinkedBoqs($scope.gridOptions.data[i])
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
        if ($scope.selectedOption != undefined) {
            var quantityCanBeAssignedFromPO = calculateQtyAssigned($scope.selectedOption);
            if (quantityCanBeAssignedFromPO > 0) {
                $scope.boqDetail.quantity = quantityCanBeAssignedFromPO;
                var product = {
                    id : $scope.selectedOption.purchaseItem.prodId,
                    prodSku : $scope.selectedOption.purchaseItem.catalogueNo
                }
                var boqDetailObject = {
                    'id':-1,
                    'supplier':$scope.purchaseOrderHeader.supplier,
                    'product':product,
                    'unitOfMeasure' : $scope.selectedOption.unitOfMeasure,
                    'cost' : $scope.selectedOption.polUnitCost,
                    'quantity' : quantityCanBeAssignedFromPO,
                    'itemValue' : quantityCanBeAssignedFromPO*$scope.selectedOption.polUnitCost ,
                    'qtyOnStock' : 0,
                    'comment' : $scope.boqDetail.comment,
                    'changeComment' : $scope.boqDetail.changeComment,
                    'qtyBalance' :  quantityCanBeAssignedFromPO,
                    'bqdCreationType' :  $scope.boqDetail.bqdCreationType,
                    'bqdStatus' :  $scope.boqDetail.bqdStatus,
                    'qtyPurchased' : quantityCanBeAssignedFromPO,
                    'qtyReceived' : 0.00
                }
                //create PO_BOQ_Link object
                var poBoqLink = {
                    id: -1,
                    pohId : $scope.purchaseOrderHeader.id,
                    pohOrderNumber : $scope.purchaseOrderHeader.pohOrderNumber,
                    polId : $scope.selectedOption.id,
                    boqId : -1,
                    boqName :'',
                    projectId:'',
                    projectCode:'',
                    boqDetailId:'',
                    boqQtyTotal:quantityCanBeAssignedFromPO,
                    poQtyReceived:0,
                    boqQtyBalance:quantityCanBeAssignedFromPO,
                    status: $scope.boqDetail.bqdStatus
                }
                var poBoqLinks = [];
                poBoqLinks.push(poBoqLink);
                boqDetailObject.linkedPurchaseOrders = poBoqLinks;
                $scope.confirm(boqDetailObject);
            } else {
                baseDataService.displayMessage('info','Invalid Quantity', 'Quantity is invalid')
            }
        }
    }

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
