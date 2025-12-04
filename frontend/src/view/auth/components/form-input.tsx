import { Field, FieldLabel } from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { Eye, EyeOff } from 'lucide-react'
import * as React from 'react'
import { useState } from 'react'
import { type FieldError } from 'react-hook-form'

// Accept Input props + new error prop
type PasswordInputProps = React.ComponentProps<typeof Input> & {
  label?: string
  error?: FieldError
}

export function PasswordInput({
  label = 'Password',
  error,
  ...props
}: PasswordInputProps) {
  const [visible, setVisible] = useState(false)

  return (
    <Field className='relative'>
      <FieldLabel htmlFor={props.id ?? 'password'}>{label}</FieldLabel>

      <div className='relative'>
        <Input
          {...props}
          id={props.id ?? 'password'}
          type={visible ? 'text' : 'password'}
          className={`
            pr-10
            ${props.className ?? ''}
            ${error ? 'border-red-500' : ''}
          `}
        />

        <button
          type='button'
          onClick={() => setVisible(!visible)}
          className='absolute right-3 top-1/2 -translate-y-1/2 text-gray-500 hover:text-gray-700'
        >
          {visible ? <EyeOff size={18} /> : <Eye size={18} />}
        </button>
      </div>

      {/* error message */}
      {error && <p className='text-red-500 text-sm mt-1'>{error.message}</p>}
    </Field>
  )
}
