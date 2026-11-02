import React from "react";

interface CardProps {
  children: React.ReactNode;
  p?: string;
  mt?: string;
  maxWidth?: string;
  className?: string;
}
export function Card({ children, p = "4", mt, maxWidth, className }: CardProps) {
  return (
    <div
      className={`bg-white border-2 border-black rounded-[8px] shadow-[4px_4px_0px_0px_#000] ${mt ? "mt-" + mt : ""} ${className ?? ""}`}
      style={{ padding: p, maxWidth, margin: "0 auto" }}
    >
      {children}
    </div>
  );
}

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: "primary" | "ghost";
  children: React.ReactNode;
  className?: string;
}
export function Button({ variant = "primary", children, className = "", ...rest }: ButtonProps) {
  return (
    <button
      className={`font-bold border-2 border-black bg-white text-black px-4 py-2 rounded-[8px] shadow-[4px_4px_0px_0px_#000] hover:shadow-none hover:translate-[2px_2px] active:translate-0 transition-all ${variant === "ghost" ? "bg-transparent text-black" : ""} ${className}`}
      {...rest}
    >
      {children}
    </button>
  );
}

interface BadgeProps extends React.HTMLAttributes<HTMLSpanElement> {
  className?: string;
  children: React.ReactNode;
}
export function Badge({ className = "", children, ...rest }: BadgeProps) {
  return (
    <span
      className={`inline-block border-2 border-black rounded px-2 py-0.5 text-sm font-bold uppercase tracking-wide ${className}`}
      {...rest}
    >
      {children}
    </span>
  );
}

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  className?: string;
}
export function Input({ label, className = "", ...rest }: InputProps) {
  return (
    <>
      {label && <label className="block font-bold mb-1">{label}</label>}
      <input
        className={`w-full border-2 border-black rounded-[8px] px-3 py-2 shadow-[4px_4px_0px_0px_#000] focus:shadow-none focus:translate-[1px_1px] outline-none ${className}`}
        {...rest}
      />
    </>
  );
}

interface LabelProps extends React.LabelHTMLAttributes<HTMLLabelElement> {
  children: React.ReactNode;
}
export function Label({ children, className = "", ...rest }: LabelProps) {
  return (
    <label className={`font-bold ${className}`} {...rest}>
      {children}
    </label>
  );
}

interface SelectProps extends React.SelectHTMLAttributes<HTMLSelectElement> {
  children: React.ReactNode;
  className?: string;
}
export function Select({ children, className = "", ...rest }: SelectProps) {
  return (
    <select
      className={`w-full border-2 border-black rounded-[8px] px-3 py-2 shadow-[4px_4px_0px_0px_#000] focus:shadow-none focus:translate-[1px_1px] outline-none ${className}`}
      {...rest}
    >
      {children}
    </select>
  );
}

export function Option({ value, children }: { value: string; children: React.ReactNode }) {
  return <option value={value}>{children}</option>;
}

export function Textarea({ value, onChange, placeholder, className = "" }: { value: string; onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void; placeholder?: string; className?: string }) {
  return (
    <textarea
      className={`w-full border-2 border-black rounded-[8px] px-3 py-2 shadow-[4px_4px_0px_0px_#000] focus:shadow-none focus:translate-[1px_1px] outline-none resize-y ${className}`}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
    />
  );
}

interface ModalProps {
  open: boolean;
  onClose: () => void;
  children: React.ReactNode;
  className?: string;
}
export function Modal({ open, onClose, children, className = "" }: ModalProps) {
  if (!open) return null;
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/50 z-50" onClick={onClose}>
      <div className="bg-white border-2 border-black rounded-[12px] shadow-[8px_8px_0px_0px_#000] p-6 w-full max-w-lg mx-auto" onClick={(e) => e.stopPropagation()}>
        <button className="absolute top-2 right-2 text-2xl border-none bg-transparent" onClick={onClose}>×</button>
        {children}
      </div>
    </div>
  );
}

Modal.Header = function Header({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return <div className={`mb-4 border-b-2 border-black pb-3 ${className}`}>{children}</div>;
};
Modal.Body = function Body({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return <div className={className}>{children}</div>;
};

interface TableProps {
  children: React.ReactNode;
  className?: string;
}
export function Table({ children, className = "" }: TableProps) {
  return <table className={`w-full border-collapse ${className}`}>{children}</table>;
}

export function TableHeader({ children }: { children: React.ReactNode }) {
  return <thead>{children}</thead>;
}

export function TableRow({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return <tr className={className}>{children}</tr>;
}

export function TableCell({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return <td className={`border border-black px-3 py-2 text-left font-medium ${className}`}>{children}</td>;
}

export function TableBody({ children }: { children: React.ReactNode }) {
  return <tbody>{children}</tbody>;
}
