package com.example.quickcash.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quickcash.R;
import com.example.quickcash.model.PaymentEmployeeModel;


import java.util.List;

/**
 * Adapter for displaying a list of employees and their corresponding job details in a RecyclerView.
 */
public class PaymentJobAdapter extends RecyclerView.Adapter<PaymentJobAdapter.JobViewHolder> {
    private List<PaymentEmployeeModel> employeeList;
    private ViewGroup parent;
    private View.OnClickListener listener;


    /**
     * ViewHolder class for holding the views corresponding to a single job item.
     */
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTitle;
        public TextView employeeName;
        public TextView paymentAmount;
        public Button selectButton;
        public ConstraintLayout paymentEmployeeLayout;


        /**
         * Constructor for JobViewHolder.
         *
         * @param itemView The root view of the job item layout.
         */
        public JobViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitleText);
            employeeName = itemView.findViewById(R.id.paymentWindowEmployeeNameText);
            paymentAmount = itemView.findViewById(R.id.paymentWindowAmountText);
            selectButton = itemView.findViewById(R.id.selectButton);
        }
    }

    /**
     * Constructor for PaymentJobAdapter.
     *
     * @param employeeList The list of employees and job details to display.
     * @param listener     A click listener for the select button in each item.
     */
    public PaymentJobAdapter(List<PaymentEmployeeModel> employeeList, View.OnClickListener listener) {
        this.employeeList = employeeList;
        this.listener = listener;
    }


    /**
     * Called to create a new ViewHolder when there are no existing reusable ViewHolders.
     *
     * @param parent   The parent ViewGroup in which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new JobViewHolder that holds the view for a job item.
     */
    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent=parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_payment_job_item, this.parent, false);
        JobViewHolder vh = new JobViewHolder(v);
        return vh;
    }


    /**
     * Binds data to the views of a ViewHolder at a given position.
     *
     * @param holder   The ViewHolder whose contents should be updated.
     * @param position The position of the item within the dataset.
     */
    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        PaymentEmployeeModel paymentEmployeeModel = employeeList.get(position);
        holder.jobTitle.setText("Job Title: " + paymentEmployeeModel.getJobTitle());
        holder.employeeName.setText("Employee Name: " + paymentEmployeeModel.getEmployeeName());
        holder.paymentAmount.setText("Payment: CAD$ " + paymentEmployeeModel.getPaymentAmount());
        holder.selectButton.setTag(paymentEmployeeModel);
        holder.selectButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(view); // Pass the view (with the tag) to the listener
            }
        });
    }


    /**
     * Returns the total number of items in the dataset.
     *
     * @return The size of the employee list.
     */
    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
