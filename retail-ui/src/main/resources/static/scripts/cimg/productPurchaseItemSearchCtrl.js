/**
 * Created by arash on 14/08/2015.
 */
cimgApp.controller('productPurchaseItemSearchCtrl', function($scope, $state, $timeout, uiGridConstants, baseDataService, searchUrl, SUCCESS, FAILURE) {

    $scope.gridOptions = {
        enableFiltering: true,
        enableSelectAll:true,
        enableRowSelection:true,
        selectionRowHeaderWidth:35,
        showGridFooter:true,
        columnDefs: [
            {field:'id', visible:false, enableCellEdit:false},
            {field:'solId', visible:false, enableCellEdit:false},
            {field:'prodId', visible:false, enableCellEdit:false},
            {field:'catalogueNo', enableCellEdit:false, width:'35%',
                headerCellTemplate: '<div ng-class="{ \'sortable\': sortable }"><div class="ui-grid-vertical-bar">&nbsp;</div><div class="ui-grid-cell-contents" col-index="renderIndex"><span>        {{ col.displayName CUSTOM_FILTERS }}</span><span ui-grid-visible="col.sort.direction" ng-class="{ \'ui-grid-icon-up-dir\': col.sort.direction == asc, \'ui-grid-icon-down-dir\': col.sort.direction == desc, \'ui-grid-icon-blank\': !col.sort.direction }">&nbsp;</span></div><div class="ui-grid-column-menu-button" ng-if="grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false" class="ui-grid-column-menu-button" ng-click="toggleMenu($event)"><i class="ui-grid-icon-angle-down">&nbsp;</i></div><div ng-if="filterable" class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><input type="text" focus-on="focusOnCatalogue" autofocus class="ui-grid-filter-input" ng-model="colFilter.term" ng-click="$event.stopPropagation()" ng-attr-placeholder="{{colFilter.placeholder || \'\'}}" /><div class="ui-grid-filter-button" ng-click="colFilter.term = null"><i class="ui-grid-icon-cancel" ng-show="!!colFilter.term">&nbsp;</i> <!-- use !! because angular interprets \'f\' as false --></div></div></div>',
                cellTooltip: function(row,col) {
                    return row.entity.catalogueNo
                }
            },
            /*
            {field:'partNo', enableCellEdit:false, width:'10%',
                cellTooltip: function(row,col) {
                    return row.entity.partNo
                }
            },
            */
            //{field:'unitOfMeasure.unomDesc', displayName:'Size',enableCellEdit:false, width:'10%'},
            //{field:'taxLegVariance.txlvDesc',displayName:'Tax',enableCellEdit:true,width:'10%'},
            {field:'costBeforeTax', displayName:'Cost(ex tax)',enableCellEdit:true, cellFilter: 'currency', width:'10%'},
            {field:'price', displayName:'Cost(inc tax)',enableCellEdit:true, cellFilter: 'currency', width:'10%'},
            {field:'rrp', displayName:'RRP',enableCellEdit:true, cellFilter: 'currency', width:'10%'},
            {field:'bulkQty', displayName:'bulkQty',enableCellEdit:false, width:'10%'},
            {field:'bulkPriceBeforeTax', displayName:'bulk price(exc tax)',enableCellEdit:true, cellFilter: 'currency', width:'10%'},
            {field:'bulkPrice', displayName:'bulk price(inc tax)',enableCellEdit:true, cellFilter: 'currency', width:'10%'}
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
            $scope.setFocusOnCatalogNo();
        });
    };
    getAllProductPurchaseItems();
    function getAllProductPurchaseItems() {
        baseDataService.getBaseData(searchUrl).then(function(response){
            $scope.gridOptions.data = response.data;
            if ($scope.gridOptions.data === undefined || $scope.gridOptions.data === null || $scope.gridOptions.data.length < 1) {
                $scope.confirm('NO_RESULT');
            }
            if ($scope.gridOptions.data.length === 1) {
                $scope.confirm($scope.gridOptions.data);
            }
            $scope.setFocusOnCatalogNo();
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
    $scope.setFocusOnCatalogNo = function () {
        /* stuff here to add a new item... */
        $scope.$broadcast('focusOnCatalogue');
    };

});
