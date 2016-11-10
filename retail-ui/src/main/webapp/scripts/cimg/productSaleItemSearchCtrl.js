/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productSaleItemSearchCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, SUCCESS, FAILURE, PRODUCT_SALE_ITEM_ALL_URI) {

    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'prodSku', enableCellEdit:false, width:'15%',
                //headerCellTemplate: '<div ng-class="{ \'sortable\': sortable }"><div class="ui-grid-vertical-bar">&nbsp;</div><div class="ui-grid-cell-contents" col-index="renderIndex"><span>Custom: {{ col.displayName CUSTOM_FILTERS }}</span><span ui-grid-visible="col.sort.direction" ng-class="{ \'ui-grid-icon-up-dir\': col.sort.direction == asc, \'ui-grid-icon-down-dir\': col.sort.direction == desc, \'ui-grid-icon-blank\': !col.sort.direction }">&nbsp;</span></div><div class="ui-grid-column-menu-button" ng-if="grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false" class="ui-grid-column-menu-button" ng-click="toggleMenu($event)"><i class="ui-grid-icon-angle-down">&nbsp;</i></div><div ng-if="filterable" class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><input type="text" class="ui-grid-filter-input" ng-model="colFilter.term" ng-click="$event.stopPropagation()" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" /><div class="ui-grid-filter-button" ng-click="colFilter.term = null"><i class="ui-grid-icon-cancel" ng-show="!!colFilter.term">&nbsp;</i> <!-- use !! because angular interprets \'f\' as false --></div></div></div>',
                headerCellTemplate: '<div ng-class="{ \'sortable\': sortable }"><div class="ui-grid-vertical-bar">&nbsp;</div><div class="ui-grid-cell-contents" col-index="renderIndex"><span>        {{ col.displayName CUSTOM_FILTERS }}</span><span ui-grid-visible="col.sort.direction" ng-class="{ \'ui-grid-icon-up-dir\': col.sort.direction == asc, \'ui-grid-icon-down-dir\': col.sort.direction == desc, \'ui-grid-icon-blank\': !col.sort.direction }">&nbsp;</span></div><div class="ui-grid-column-menu-button" ng-if="grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false" class="ui-grid-column-menu-button" ng-click="toggleMenu($event)"><i class="ui-grid-icon-angle-down">&nbsp;</i></div><div ng-if="filterable" class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><input type="text" focus-on="focusOnMe" autofocus class="ui-grid-filter-input" ng-model="colFilter.term" ng-click="$event.stopPropagation()" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" /><div class="ui-grid-filter-button" ng-click="colFilter.term = null"><i class="ui-grid-icon-cancel" ng-show="!!colFilter.term">&nbsp;</i> <!-- use !! because angular interprets \'f\' as false --></div></div></div>',

                cellTooltip: function(row,col) {
                    return row.entity.prodSku
                }
            },
            {field:'reference', enableCellEdit:false, width:'15%',
                cellTooltip: function(row,col) {
                    return row.entity.reference
                }
            },
            {field:'prodName', displayName:'Name',enableCellEdit:false, width:'30%',
                cellTooltip: function(row,col) {
                    return row.entity.prodName
                }
            },
            {field: 'prodDesc', displayName: 'Description', enableCellEdit: false, width: '20%',
                cellTooltip: function (row, col) {
                    return row.entity.prodDesc
                }
            },
            {field:'prodBrand', displayName:'Brand',enableCellEdit:false, width:'10%'},
            {field:'prodClass', displayName:'Class',enableCellEdit:false, width:'10%'}
        ]
    }
    $scope.gridOptions.multiSelect = true;
    //$scope.gridOptions.noUnselect= true;

    //
    $scope.gridOptions.onRegisterApi = function (gridApi) {
        $scope.gridApi = gridApi;
        $scope.gridApi.core.on.sortChanged( $scope, function( grid, sort ) {
            $scope.gridApi.core.notifyDataChange( $scope.gridApi.grid, uiGridConstants.dataChange.COLUMN );
        })
        gridApi.selection.on.rowSelectionChanged($scope, function(row) {
            //baseDataService.setRow(row.entity);
            $scope.selectedOption = row.entity;
            gridApi.grid.clearAllFilters();
            $scope.setFocusOnSku();
        });
    };
    getAllProductSaleItems();
    function getAllProductSaleItems() {
        baseDataService.getBaseData(PRODUCT_SALE_ITEM_ALL_URI).then(function(response){
            var data = angular.copy(response.data);
            $scope.gridOptions.data = data;
            $scope.setFocusOnSku();
        });
    }

    $scope.submit = function () {
        if ($scope.selectedOption != undefined) {
            //$scope.confirm($scope.selectedOption);
            $scope.confirm($scope.gridApi.selection.getSelectedRows());
        }
    }

    $scope.cancel = function() {
        $scope.closeThisDialog('button');
    }

    $scope.setFocusOnSku = function () {
        /* stuff here to add a new item... */
        $scope.$broadcast('focusOnMe');
    };

});
