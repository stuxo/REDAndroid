package ch.redacted.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.redacted.app.R;

/**
 * Created by sxo on 29/12/16.
 */

public class ForumNavigationView extends LinearLayout implements View.OnClickListener {

    @BindView(R.id.current_page)
    Button currentPage;

    @BindView(R.id.first_page)
    ImageButton firstPage;

    @BindView(R.id.back_page)
    ImageButton backPage;

    @BindView(R.id.next_page)
    ImageButton nextPage;

    @BindView(R.id.last_page)
    ImageButton lastPage;

    OnNavigationEventListener mListener;

    private int currentPageNumber = 1;
    private int maxPageNumber = 1;

    public ForumNavigationView(Context context) {
        super(context);
        init();
    }

    public ForumNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ForumNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setPageCount(int maxPages) {
        maxPageNumber = maxPages;
        currentPage.setText(String.format("%d / %d", currentPageNumber, maxPageNumber));
        if (maxPages > 1) {
            this.setVisibility(View.VISIBLE);
        }
        invalidate();
        requestLayout();
    }

    public void init() {
        inflate(getContext(), R.layout.forum_navigation, this);
        ButterKnife.bind(this);
        updateCurrentPageNumber();
        firstPage.setOnClickListener(this);
        backPage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
        lastPage.setOnClickListener(this);
        currentPage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createNumberPickerDialog().show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.first_page:
                if (currentPageNumber != 1) {
                    currentPageNumber = 1;
                    mListener.onNavigationChanged();
                }
                break;
            case R.id.back_page:
                if (currentPageNumber > 1) {
                    currentPageNumber--;
                    mListener.onNavigationChanged();
                }
                break;
            case R.id.next_page:
                if (maxPageNumber > currentPageNumber) {
                    currentPageNumber++;
                    mListener.onNavigationChanged();
                }
                break;
            case R.id.last_page:
                if (currentPageNumber != maxPageNumber) {
                    currentPageNumber = maxPageNumber;
                    mListener.onNavigationChanged();
                }
                break;
            default:
                break;
        }
        updateCurrentPageNumber();
    }

    public void updateCurrentPageNumber(int page) {
        currentPageNumber = page;
        currentPage.setText(String.format("%d / %d", currentPageNumber, maxPageNumber));
        invalidate();
        requestLayout();
    }

    private void updateCurrentPageNumber() {
        currentPage.setText(String.format("%d / %d", currentPageNumber, maxPageNumber));
        invalidate();
        requestLayout();
    }

    public interface OnNavigationEventListener {
        void onNavigationChanged();
    }

    public void setNavigationEventListener(OnNavigationEventListener eventListener) {
        mListener = eventListener;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public AlertDialog createNumberPickerDialog()
    {
        final NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMaxValue(maxPageNumber);
        numberPicker.setMinValue(1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(numberPicker);
        builder.setTitle(getContext().getString(R.string.select_page));
        builder.setPositiveButton(getContext().getString(R.string.dialog_action_ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentPageNumber = numberPicker.getValue();
                updateCurrentPageNumber();
                mListener.onNavigationChanged();
                dialog.dismiss();
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(getContext().getString(R.string.dialog_action_cancel), new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
