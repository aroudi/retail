/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productPriceChangeBulkCtrl', function($scope, $state, $timeout, uiGridConstants,ngDialog, baseDataService, SUCCESS, FAILURE, SUPPLIER_GET_PRODUCT_LIST_WITH_PRICE_URI, SUPPLIER_ALL_URI, PRODUCT_PRICE_UPDATE_BULK) {

    $scope.model= {};
    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:false,
        enableRowSelection:false,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'solId', visible:false, enableCellEdit:false},
            {field:'prodId', visible:false, enableCellEdit:false},
            {field:'catalogueNo', enableCellEdit:false, width:'20%',
                headerCellTemplate: '<div ng-class="{ \'sortable\': sortable }"><div class="ui-grid-vertical-bar">&nbsp;</div><div class="ui-grid-cell-contents" col-index="renderIndex"><span>        {{ col.displayName CUSTOM_FILTERS }}</span><span ui-grid-visible="col.sort.direction" ng-class="{ \'ui-grid-icon-up-dir\': col.sort.direction == asc, \'ui-grid-icon-down-dir\': col.sort.direction == desc, \'ui-grid-icon-blank\': !col.sort.direction }">&nbsp;</span></div><div class="ui-grid-column-menu-button" ng-if="grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false" class="ui-grid-column-menu-button" ng-click="toggleMenu($event)"><i class="ui-grid-icon-angle-down">&nbsp;</i></div><div ng-if="filterable" class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><input type="text" focus-on="focusOnCatalogue" autofocus class="ui-grid-filter-input" ng-model="colFilter.term" ng-click="$event.stopPropagation()" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" /><div class="ui-grid-filter-button" ng-click="colFilter.term = null"><i class="ui-grid-icon-cancel" ng-show="!!colFilter.term">&nbsp;</i> <!-- use !! because angular interprets \'f\' as false --></div></div></div>',
                cellTooltip: function(row,col) {
                    return row.entity.catalogueNo
                }
            },
            {field:'partNo', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.partNo
                }
            },
            {field:'costBeforeTax', displayName:'Cost',enableFiltering:false,enableCellEdit:false, cellFilter: 'currency', width:'7%'},
            {field:'newPrice', displayName:'New Cost',enableFiltering:false,enableCellEdit:true, cellFilter: 'currency', width:'8%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'rrp', displayName:'RRP',enableCellEdit:false, enableFiltering:false, cellFilter: 'currency', width:'7%'},
            {field:'newRrp', displayName:'New RRP',enableCellEdit:true,enableFiltering:false, cellFilter: 'currency', width:'8%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'bulkPrice', displayName:'Bulk',enableCellEdit:false,enableFiltering:false, cellFilter: 'currency', width:'7%'},
            {field:'newBulkPrice', displayName:'New Bulk',enableCellEdit:true,enableFiltering:false, cellFilter: 'currency', width:'8%',
                cellClass: function (grid, row, col, rowRenderIndex, colRenderIndex) {
                    return 'editModeColor'
                }
            },
            {field:'percentage', displayName:'Percentage',enableCellEdit:false,enableFiltering:true, cellFilter: 'number', width:'7%'},
            {field:'changed', displayName:'Price Changed', cellFilter:'booleanFilter' ,enableCellEdit:false, width:'8%',
                filter :{type: uiGridConstants.filter.SELECT, selectOptions:[{value:true, label:'Yes'},{value:false, label:'No'}]},
                cellClass:
                    function(grid, row, col, rowRenderIndex, colRenderIndex) {
                        if (grid.getCellValue(row, col) === true) {
                            return 'green';
                        } else {
                            return 'red'
                        }
                    }
            }

        ],
        exporterLinkLabel: 'get your csv here',
        exporterPdfDefaultStyle: {fontSize: 9},
        exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
        exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
        exporterPdfOrientation: 'portrait',
        exporterPdfPageSize: 'LETTER',
        exporterPdfMaxGridWidth: 500,
        exporterHeaderFilter: function( displayName ) {
            return displayName;
        },
        exporterFieldCallback: function( grid, row, col, input ) {
            return input;
        }

    };

    $scope.$on('uiGridEventEndCellEdit', function (event) {
        var product = event.targetScope.row.entity;
            product['changed'] = true;
    });

    $scope.gridOptions.multiSelect = false;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
    };
    initForm();
    function initForm() {
        baseDataService.getBaseData(SUPPLIER_ALL_URI).then(function(response){
            $scope.supplierSet = response.data;
            $scope.model.supplier = baseDataService.populateSelectList($scope.model.supplier,$scope.supplierSet);
            //$scope.changeSupplier();
            getSupplierProducts();
        });
    };
    function getSupplierProducts() {
        baseDataService.getBaseData(SUPPLIER_GET_PRODUCT_LIST_WITH_PRICE_URI+$scope.model.supplier.id).then(function(response){
            //var data = angular.copy(response.data);
            //add dummy fields to the list.

            $scope.gridOptions.data = response.data;
            resetNewPrices($scope.gridOptions.data);
        });
    }

    $scope.onSupplierChange = function () {
        $scope.gridApi.grid.clearAllFilters();
        getSupplierProducts();
    };



    $scope.submit = function () {
        removeDummyFields($scope.gridOptions.data);
        baseDataService.addRow($scope.gridOptions.data, PRODUCT_PRICE_UPDATE_BULK).then(function(response) {
            addResponse = response.data;
            if (addResponse.status == SUCCESS ) {
                    baseDataService.displayMessage("info","update was successful", "Price list was updated successfully");
            } else {
                baseDataService.displayMessage("info","Warning", "there was a problem in updating price list. please contact the technical board");
            }
        });

    };

    $scope.resetPrice = function() {
        resetNewPrices($scope.gridOptions.data);
    };

    function resetNewPrices(priceList) {
        if (priceList === undefined || priceList.length < 1) {
            return;
        }
        for (i=0; i<priceList.length; i++) {
            priceList[i].newPrice = priceList[i].costBeforeTax;
            priceList[i].newRrp = priceList[i].rrp;
            priceList[i].newBulkPrice = priceList[i].bulkPrice;
            priceList[i].changed = false;
            priceList[i].percentage = 0.00;
        }
    }
    function removeDummyFields(priceList) {
        if (priceList === undefined || priceList.length < 1) {
            return;
        }
        for (i=0; i<priceList.length; i++) {
            priceList[i].costBeforeTax = priceList[i].newPrice;
            priceList[i].rrp = priceList[i].newRrp;
            priceList[i].bulkPrice = priceList[i].newBulkPrice;

            delete priceList[i].newPrice;
            delete priceList[i].newRrp;
            delete priceList[i].newBulkPrice;
            //delete priceList[i].changed;
            delete priceList[i].percentage;
        }
    }

    $scope.applyPercentage = function () {
        if ($scope.gridOptions.data === undefined || $scope.gridOptions.data.length < 1) {
            return;
        }
        var filteredRows = $scope.gridApi.core.getVisibleRows($scope.gridApi.grid);
        for (i=0; i<filteredRows.length; i++) {
            if ($scope.changeCost) {
                filteredRows[i].entity.newPrice = filteredRows[i].entity.costBeforeTax + (filteredRows[i].entity.costBeforeTax * $scope.percentage)/100;
                filteredRows[i].entity.changed = true;
                filteredRows[i].entity.percentage = $scope.percentage;
            }
            if ($scope.changeRrp) {
                filteredRows[i].entity.newRrp = filteredRows[i].entity.rrp + (filteredRows[i].entity.rrp * $scope.percentage) / 100;
                filteredRows[i].entity.changed = true;
                filteredRows[i].entity.percentage = $scope.percentage;
            }
            if ($scope.changeBulk) {
                filteredRows[i].entity.newBulkPrice = filteredRows[i].entity.bulkPrice + (filteredRows[i].entity.bulkPrice * $scope.percentage)/100;
                filteredRows[i].entity.changed = true;
                filteredRows[i].entity.percentage = $scope.percentage;
            }
        }

    };
    $scope.exportCSV = function() {
        var myElement = angular.element(document.querySelectorAll(".custome-csv-link-location"));
        $scope.gridOptions.exporterCsvFilename = $scope.model.supplier.supplierName + '_product_list.csv';
        $scope.gridApi.exporter.csvExport('all','all', myElement);
    }

    $scope.importFromCsv = function () {

        ngDialog.openConfirm({
            template:'views/pages/importForm.html',
            controller:'importCtrl',
            className: 'ngdialog-theme-default',
            closeByDocument:false,
            resolve: {importEntity: function(){return 'productPrice'}}
        }).then (function (result){
                getSupplierProducts();
            }, function(reason) {
                console.log('Modal promise rejected. Reason:', reason);
            }
        );

    }
});
