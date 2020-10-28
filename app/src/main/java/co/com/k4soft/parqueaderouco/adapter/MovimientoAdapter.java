package com.example.parqueaderouco.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parqueaderouco.R;
import com.example.parqueaderouco.entidades.Movimiento;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovimientoAdapter  extends BaseAdapter implements Filterable {

    private final LayoutInflater inflater;
    private List<Movimiento> listaMovimientosOut;
    private List<Movimiento> listaMovimientosIn;

    public MovimientoAdapter(Context context, List<Movimiento> listaMovimientos) {
        listaMovimientosOut = listaMovimientos;
        listaMovimientosIn = listaMovimientos;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listaMovimientosOut.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaMovimientosOut = (List<Movimiento>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Movimiento> FilteredArrList = new ArrayList<>();
                if (listaMovimientosIn == null) {
                    listaMovimientosIn = new ArrayList<>(listaMovimientosOut);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = listaMovimientosIn.size();
                    results.values = listaMovimientosIn;
                } else {

                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < listaMovimientosIn.size(); i++) {
                        String data = listaMovimientosIn.get(i).getPlaca();
                        if (data.toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(listaMovimientosIn.get(i));
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    @Override
    public Movimiento getItem(int position) {
        return listaMovimientosOut.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }


        holder.placa.setText(listaMovimientosOut.get(position).getPlaca());
        holder.fechaIngreso.setText(listaMovimientosOut.get(position).getFechaEntrada());
        holder.fechaSalida.setText(listaMovimientosOut.get(position).getFechaSalida());
        holder.valorTotal.setText(listaMovimientosOut.get(position).getValorTotal());
        if(!listaMovimientosOut.get(position).isFinalizaMovimiento()){
            holder.placa.setTextColor(Color.RED);
            holder.fechaSalida.setText("Vehículo sin salir del parqueadero");
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.lbPlaca)
        TextView placa;
        @BindView(R.id.lbingreso)
        TextView fechaIngreso;
        @BindView(R.id.lbsalida)
        TextView fechaSalida;
        @BindView(R.id.lbvalorTotal)
        TextView valorTotal;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}