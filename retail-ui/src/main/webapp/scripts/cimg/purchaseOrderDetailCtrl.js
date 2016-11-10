/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('purchaseOrderDetailCtrl', function($filter, $scope,uiGridConstants, $state,ngDialog, $timeout,baseDataService, SUCCESS, FAILURE, POL_CREATION_TYPE_MANUAL, POH_SAVE_URI, POH_STATUS_URI, POH_UPDATE_LINKED_BOQS_URI, POH_STATUS_IN_PROGRESS, POH_EXPORT_PDF, POH_STATUS_CONFIRMED, POH_STATUS_CANCELLED, GET_PURCHASE_ITEM_PER_SUPPLIER_CATALOG_URI) {
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
            {field:'polUnitCost', displayName:'Cost',enableCellEdit:true, width:'10%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polQtyOrdered', displayName:'Qty Ordered',enableCellEdit:true, width:'10%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'polValueOrdered', displayName:'Value',enableCellEdit:false, width:'7%', cellFilter: 'currency', footerCellFilter: 'currency', aggregationType: uiGridConstants.aggregationTypes.sum},
            {field:'polQtyReceived', displayName:'Qty Received', enableCellEdit:false,width:'8%',type: 'number', aggregationType: uiGridConstants.aggregationTypes.sum,
                cellTooltip: function(row,col) {
                    return row.entity.polFreeText
                }
            },
            {field:'polCreationType', displayName:'Type',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {field:'polStatus', displayName:'Status',enableCellEdit:false, width:'10%', cellFilter:'configCategoryFilter',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return grid.getCellValue(row, col).color
                }
            },
            {name:'Action', sortable:false,enableFiltering:false,enableCellEdit:false, cellTemplate:'<a href=""><i tooltip="delete item" tooltip-placement="bottom" class="fa fa-remove fa-2x" ng-click="grid.appScope.removeItem(row)"></i></a> <a href=""><i tooltip="update BOQ" tooltip-placement="bottom" class="fa fa-edit fa-2x" ng-show="row.entity.polStatus.categoryCode==\'POH_STATUS_GOOD_RECEIVED\' || row.entity.polStatus.categoryCode==\'POH_STATUS_PARTIAL_REC\'" ng-click="grid.appScope.updateBoqQtyRcvd(row)"></i></a>', width: '5%'}
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
            if (colDef.name == 'polQtyOrdered') {
                $scope.polQtyOrderedBeforeEditting = rowEntity.polQtyOrdered;
            }
            if (colDef.name == 'polUnitCost') {
                $scope.polUnitCostBeforeEditting = rowEntity.polUnitCost;
            }
        })

        gridApi.edit.on.afterCellEdit($scope, function(rowEntity, colDef){
            if (colDef.name == 'polQtyOrdered') {
                if (rowEntity.polCreationType.displayName=='AUTO') {
                    if (rowEntity.polQtyOrdered < getLinkeBoqQtyBalanceTotal(rowEntity)) {
                        baseDataService.displayMessage('info','Warning!!','You can not decrease quantity for auto created item.');
                        rowEntity.polQtyOrdered = $scope.polQtyOrderedBeforeEditting;
                        return;
                    }
                }
                //update the total value of the line
                updatePurchaseLineValueOrdered(rowEntity);
            }
            if (colDef.name == 'polUnitCost') {
                if (rowEntity.polUnitCost > rowEntity.purchaseItem.price) {
                    baseDataService.displayMessage('info','Warning!!','The new price is more than item original price which is: ' + rowEntity.purchaseItem.price);
                    rowEntity.polUnitCost = $scope.polUnitCostBeforeEditting;
                    return;
                }
                //update the total value of the line
                updatePurchaseLineValueOrdered(rowEntity);
            }
        })
    };
    /*
    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var purchaseLine = event.targetScope.row.entity;
        cellData = event.targetScope.row.entity[event.targetScope.col.field];
        if (purchaseLine.polCreationType.displayName=='AUTO') {
            if (cellData < getLinkeBoqQtyBalanceTotal(purchaseLine)) {
                baseDataService.displayMessage('Warning!!','You can not decrease quantity for auto created item.');
                purchaseLine.polQtyOrdered = $scope.polQtyOrderedBeforeEditting;
                return;
            }
        }
        //update the total value of the line
        updatePurchaseLineValueOrdered(purchaseLine);
    })
    */
    initPageData();
    function initPageData() {
        $scope.posIsCancessled = false;
        $scope.posIsConfirmed = false;
        baseDataService.getBaseData(POL_CREATION_TYPE_MANUAL).then(function(response){
            var data = angular.copy(response.data);
            $scope.polCreationTypeManual = data;
        });
        baseDataService.getBaseData(POH_STATUS_IN_PROGRESS).then(function(response){
            var data = angular.copy(response.data);
            $scope.statusOnProgress = data;
        });
        baseDataService.getBaseData(POH_STATUS_CONFIRMED).then(function(response){
            var data = angular.copy(response.data);
            $scope.statusConfirmed = data;
        });
        baseDataService.getBaseData(POH_STATUS_CANCELLED).then(function(response){
            var data = angular.copy(response.data);
            $scope.statusCancelled = data;
        });
        baseDataService.getBaseData(POH_STATUS_URI).then(function(response){
            $scope.pohStatusSet = response.data;
            /*
             $scope.pohStatusSet = [];
             var arr = response.data;
             //we shouldn't display PARTIAL REC and GOOD RECEIVED
             for (i=0; i< arr.length; i++) {
             if (arr[i].displayName !='PARTIAL REC' && arr[i].displayName !='GOOD RECEIVED' ) {
             $scope.pohStatusSet.push(arr[i]);
             }
             }
             */
            $scope.purchaseOrderHeader.pohStatus = baseDataService.populateSelectList($scope.purchaseOrderHeader.pohStatus,$scope.pohStatusSet);
            managePoStatusDisplay();
        });
        if ( baseDataService.getIsPageNew()) {
            $scope.purchaseOrderHeader = {};
            $scope.purchaseOrderHeader.pohStatus = $scope.statusOnProgress;
            //$scope.purchaseOrderHeader.pohCreatedDate = new Date().getTime();
            $scope.purchaseOrderHeader.pohExpDeliveryStr = new Date();
            $scope.purchaseOrderHeader.pohCreatedDateStr = new Date();
            $scope.purchaseOrderHeader.id = -1;
            $scope.pageIsNew = true;
        } else {
            $scope.purchaseOrderHeader = angular.copy(baseDataService.getRow());
            $scope.gridOptions.data = $scope.purchaseOrderHeader.lines;
            $scope.purchaseOrderHeader.pohExpDeliveryStr = new Date($scope.purchaseOrderHeader.pohExpDeliveryStr);
            $scope.purchaseOrderHeader.pohCreatedDateStr = new Date($scope.purchaseOrderHeader.pohCreatedDateStr);

            for (i=0; i<$scope.gridOptions.data.length; i++) {
                displayLinkedBoqs($scope.gridOptions.data[i])
            }
            baseDataService.setRow({});
            baseDataService.setIsPageNew(true);
            $scope.pageIsNew = false;
        }

    }

    $scope.cancelForm = function() {
        $state.go($scope.previouseState);
    }

    $scope.searchProduct = function () {
        if ($scope.purchaseOrderHeader.supplier === undefined) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        ngDialog.openConfirm({
            template:'views/pages/productSaleItemSearch.html',
            controller:'productPurchaseItemSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {supplier: function(){return $scope.purchaseOrderHeader.supplier}}
        }).then (function (selectedItems){
                if (selectedItems != undefined) {
                    for (var i = 0; i < selectedItems.length; i++) {
                        var selectedProduct = selectedItems[i];
                        if (checkIfProductHasBeenSelected(selectedProduct)) {
                            continue;
                        }
                        var purchaseLine = createPurchaseLine(selectedProduct);
                        $scope.gridOptions.data.push(purchaseLine);
                    }
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };
    function createPurchaseLine (item) {
        var rowId;
        if ($scope.gridOptions.data == undefined && $scope.gridOptions.data ==null) {
            rowId = -2000;
        } else {
            rowId = $scope.gridOptions.data.length - 2000;  //in case of having record, don't mixed up with existing recoreds.
        }
        var purchaseLineObject = {
            'id':rowId,
            'pohId':$scope.purchaseOrderHeader.id,
            'pohOrderNumber':$scope.purchaseOrderHeader.pohOrderNumber,
            'purchaseItem' : item,
            'polUnitCost' : item.price,
            'unitOfMeasure' : item.unitOfMeasure,
            'polQtyOrdered' : 0.00,
            'polValueOrdered' : 0.00,
            'unomContents' : item.unitOfMeasure,
            'polCreationType' :  $scope.polCreationTypeManual,
            'polStatus' :  $scope.statusOnProgress
            //polStatus
        }
        return purchaseLineObject;
    }

    function getLinkeBoqQtyBalanceTotal(purchaseLine) {
        var boqQtyBalanceTotal = 0.0;
        var boqLinkedList = purchaseLine.poBoqLinks;
        if (boqLinkedList == undefined || boqLinkedList.length < 1) {
            return boqQtyBalanceTotal;
        }
        for (var i = 0; i < boqLinkedList.length; i++) {
            if (boqLinkedList[i].boqQtyBalance != undefined) {
                boqQtyBalanceTotal = boqQtyBalanceTotal + boqLinkedList[i].boqQtyBalance;
            }
        }
        return boqQtyBalanceTotal;

    }

    function updatePurchaseLineValueOrdered(line) {
        if (line == undefined) {
            return;
        }
        line.polValueOrdered = line.polUnitCost * line.polQtyOrdered;
    }

    $scope.savePoAsDraft = function() {
        $scope.purchaseOrderHeader.pohStatus = $scope.statusOnProgress;
        savePurchaseOrder('draft');
    }

    $scope.submitPo = function() {
        $scope.purchaseOrderHeader.pohStatus = $scope.statusConfirmed;
        savePurchaseOrder('submit');
    }

    function savePurchaseOrder(mode) {

        //$scope.facility.lastModifiedBy = userId;
        var rowObject = $scope.purchaseOrderHeader;
        if ($scope.pageIsNew) {
            $scope.purchaseOrderHeader.lines = $scope.gridOptions.data
        } else {
            //remove the subgrid info from data before submitting:
            for (i=0; i<$scope.gridOptions.data.length; i++) {
                if ($scope.gridOptions.data[i].subGridOptions != undefined || $scope.gridOptions.data[i].subGridOptions != null) {
                    delete $scope.gridOptions.data[i].subGridOptions;
                }
            }
        }
        baseDataService.addRow(rowObject, POH_SAVE_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                if ($scope.pageIsNew) {
                    baseDataService.displayMessage("info","Order Number", "Purhcase order saved with number: " + addResponse.info);
                }
                if (mode == 'draft') {
                    $state.go('dashboard.purchaseOrderList');
                } else {
                    exportToPdf();
                }
            } else {
                alert('Not able to save purchase order. ' + addResponse.message);
            }
        });
        return;
    }

    $scope.updateLinkedBoqs = function () {

        /*
         var userId = UserService.getUserId();
         if (userId == undefined || userId == 0) {
         alert('you need to login first');
         $state.go('dashboard.login');
         }
         */

        //$scope.facility.lastModifiedBy = userId;
        $scope.purchaseOrderHeader.lines = $scope.gridOptions.data;
        var rowObject = $scope.purchaseOrderHeader;
        //remove the subgrid info from data before submitting:
        for (i=0; i<$scope.gridOptions.data.length; i++) {
            if ($scope.gridOptions.data[i].subGridOptions != undefined || $scope.gridOptions.data[i].subGridOptions != null) {
                delete $scope.gridOptions.data[i].subGridOptions;
            }
        }
        baseDataService.addRow(rowObject, POH_UPDATE_LINKED_BOQS_URI).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                $state.go('dashboard.purchaseOrderList');
            } else {
                baseDataService.displayMessage("info","Error in saving data", addResponse.message);
            }
        });
        return;
    }


    $scope.removeItem = function(row) {
        if (row == undefined || row.entity == undefined) {
            alert('item is undefined');
            return;
        }
        if (row.entity.polCreationType.displayName=='AUTO') {
            return;
        }
        if ($scope.purchaseOrderHeader.pohStatus.categoryCode != 'POH_STATUS_IN_PROGRESS' && row.entity.id>=0) {
            return;
        }
        baseDataService.displayMessage('yesNo','Warning!!','Are you sure you want to delete this item?').then(function(result){
            if (result) {
                row.entity.deleted = true;
                $scope.gridApi.core.setRowInvisible(row);
            } else {
                return;
            }
        });
    }

    $scope.searchSupplier = function () {
        ngDialog.openConfirm({
            template:'views/pages/supplierSearch.html',
            controller:'supplierSearchCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false
        }).then (function (value){
                //alert('returned value = ' + value);
                $scope.purchaseOrderHeader.supplier = value;
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    };

    $scope.formatDate = function(value) {
        return $filter('date')(value, 'medium');
    }

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
                {name:"projectName", field:"projectName", displayName:"Project", width:'25%',
                    cellTooltip: function(row,col) {
                        return row.entity.projectName
                    }
                },
                {name:"projectCode", field:"projectCode", displayName:"Project No", width:'25%',
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
    $scope.updateBoqQtyRcvd = function (row) {
        var purchaseLine = row.entity;

        ngDialog.openConfirm({
            template:'views/pages/purchaseLineBoqDetail.html',
            controller:'purchaseLineBoqDetailCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {purchaseOrderLine: function(){return purchaseLine}}
        }).then (function (updatedLine){
                if (updatedLine != undefined) {
                    row.entity = updatedLine;
                }
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );
    }

    function exportToPdf () {
        var exportUrl = POH_EXPORT_PDF + $scope.purchaseOrderHeader.id;
        baseDataService.getStreamData(exportUrl).then(function(response){
            var blob = new Blob([response.data], {'type': 'application/pdf'});
            //saveAs (blob , outPutFile);
            var myPdfContent = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            baseDataService.setPdfContent(myPdfContent);
            $state.go('dashboard.pdfViewer');

            /*
            hiddenElement.href = window.URL.createObjectURL(blob);//'data:attachment/'+fileFormat+',' + encodeURI(response.data);
            //hiddenElement.target ='_blank';
            hiddenElement.download = 'purchaseOrder.pdf';
            hiddenElement.click();
            */
        });

    }
    function managePoStatusDisplay(){
        if ($scope.purchaseOrderHeader.pohStatus.categoryCode == 'POH_STATUS_CONFIRMED') {
            $scope.poIsConfirmed = true;
            $scope.poIsCancelled = false;
        }
        if ($scope.purchaseOrderHeader.pohStatus.categoryCode == 'POH_STATUS_CANCELLED') {
            $scope.poIsConfirmed = false;
            $scope.poIsCancelled = true;
        }

    }

    function checkIfProductHasBeenSelected(item) {

        for (var i = 0; i<$scope.gridOptions.data.length; i++) {
            if ($scope.gridOptions.data[i].purchaseItem.prodId == item.prodId) {
                return true;
            }
        }
        return false;
    }

    $scope.searchProductByCatalogNo = function(keyEvent) {
        if (keyEvent.which != 13) {
            return
        }
        if ($scope.purchaseOrderHeader.supplier === undefined) {
            baseDataService.displayMessage('info','Warning!','Please select supplier');
            return;
        }
        var searchUri = GET_PURCHASE_ITEM_PER_SUPPLIER_CATALOG_URI + $scope.purchaseOrderHeader.supplier.id + '/' + $scope.searchByCatalog;
        baseDataService.getBaseData(searchUri).then(function(response){
            var selectedProduct = response.data;
            if (selectedProduct === null || selectedProduct === undefined || selectedProduct.catalogueNo == undefined) {
                baseDataService.displayMessage('info','Warning!','product not found!!!');
                return;
            }
            if (checkIfProductHasBeenSelected(selectedProduct)) {
                return;
            }
            var purchaseLine = createPurchaseLine(selectedProduct);
            $scope.gridOptions.data.push(purchaseLine);
        });
    }

});
