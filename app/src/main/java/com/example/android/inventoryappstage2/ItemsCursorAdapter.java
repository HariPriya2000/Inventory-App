package com.example.android.inventoryappstage2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryappstage2.Data.InventoryContract.InventoryEntry;

/**
 * {@link ItemsCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of item data as its data source. This adapter knows
 * how to create list items for each row of item data in the {@link Cursor}.
 */
public class ItemsCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ItemsCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ItemsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView productNameTextView = view.findViewById(R.id.display_name);
        TextView productPriceTextView = view.findViewById(R.id.display_price);
        TextView productQuantityTextView = view.findViewById(R.id.display_quantity);

        // Find the columns of item attributes that we're interested in
        int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int productPriceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
        int productQuantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);

        // Read the item attributes from the Cursor for the current item
        String productName = cursor.getString(productNameColumnIndex);
        int productPrice = cursor.getInt(productPriceColumnIndex);
        int productQuantity = cursor.getInt(productQuantityColumnIndex);

        // Update the TextViews with the attributes for the current item
        productNameTextView.setText(productName);
        productPriceTextView.setText(String.valueOf(productPrice));
        productQuantityTextView.setText(String.valueOf(productQuantity));

        // column number of "_ID"
        int productIdColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);

        // Read the item attributes from the Cursor for the current item for "Sale" button
        final long productIdVal = Integer.parseInt(cursor.getString(productIdColumnIndex));
        final int currentProductQuantityVal = cursor.getInt(productQuantityColumnIndex);

        /*
         * Each list view item will have a "Sale" button
         * This "Sale" button has OnClickListener which will decrease the product quantity by one at a time.
         * Update is only carried out if quantity is greater than 0(i.e MINIMUM quantity is 0).
         */
        Button saleButton = view.findViewById(R.id.button_sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri currentUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, productIdVal);

                String updatedQuantity = String.valueOf(currentProductQuantityVal - 1);

                if (Integer.parseInt(updatedQuantity) >= 0) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, updatedQuantity);
                    context.getContentResolver().update(currentUri, values, null, null);
                }
            }
        });
    }
}