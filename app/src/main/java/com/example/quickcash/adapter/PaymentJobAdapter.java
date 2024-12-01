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

public class PaymentJobAdapter extends RecyclerView.Adapter<PaymentJobAdapter.JobViewHolder> {
    private List<PaymentEmployeeModel> employeeList;
    private ViewGroup parent;
    private OnItemSelectedListener listener;


    public static class JobViewHolder extends RecyclerView.ViewHolder {
        public TextView jobTitle;
        public TextView employeeName;
        public TextView paymentAmount;
        public Button selectButton;
        public ConstraintLayout paymentEmployeeLayout;


        public JobViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitleText);
            employeeName = itemView.findViewById(R.id.paymentWindowEmployeeNameText);
            paymentAmount = itemView.findViewById(R.id.paymentWindowAmountText);
            selectButton = itemView.findViewById(R.id.selectButton);
        }
    }

    public interface OnItemSelectedListener{
        void onItemSelected(PaymentEmployeeModel item);
    }


    public PaymentJobAdapter(List<PaymentEmployeeModel> employeeList, OnItemSelectedListener listener) {
        this.employeeList = employeeList;
        this.listener = listener;
    }


    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent=parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_payment_job_item, this.parent, false);
        JobViewHolder vh = new JobViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        PaymentEmployeeModel paymentEmployeeModel = employeeList.get(position);
        holder.jobTitle.setText("Job Title: " + paymentEmployeeModel.getJobTitle());
        holder.employeeName.setText("Company: " + paymentEmployeeModel.getEmployeeName());
        holder.paymentAmount.setText("Location: " + paymentEmployeeModel.getPaymentAmount());
        holder.selectButton.setOnClickListener(view -> {
            if (listener!=null){
                listener.onItemSelected(paymentEmployeeModel);
            }
        });
    }


    @Override
    public int getItemCount() {
        return employeeList.size();
    }
}
